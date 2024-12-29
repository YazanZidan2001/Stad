package com.example.stad.WebApi.Controller.Owner;

import com.example.stad.Common.Entities.Stadium;
import com.example.stad.Common.Entities.User;
import com.example.stad.Common.Enums.Role;
import com.example.stad.Core.Services.AuthenticationService;
import com.example.stad.Core.Services.StadiumService;
import com.example.stad.SessionManagement;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/stadium")
@RequiredArgsConstructor
public class StadiumController extends SessionManagement {

    private final StadiumService stadiumService;
    private final AuthenticationService authenticationService;

    private static final String uploadDir="uploads/";

    @PostConstruct
    public void ensureUploadDirectoryExists() {
        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    @PostMapping("/uploadImage")
    public ResponseEntity<String> uploadStadiumImage(@RequestParam("image") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            // Generate unique file name
            String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);

            // Save the file
            Files.write(filePath, file.getBytes());

            // Construct the URL to access the image
            String imageUrl = "/uploads/" + fileName;

            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Failed to upload image");
        }
    }

    @GetMapping("/uploads/{fileName}")
    public ResponseEntity<Resource> serveImage(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    private String saveImageWithCustomName(MultipartFile image, String stadiumName, String ownerId, String type) throws IOException {
        // Validate and extract the file extension from the original file name
        String originalFileName = image.getOriginalFilename();
        if (originalFileName == null || !originalFileName.contains(".")) {
            throw new IllegalArgumentException("Invalid file type. The file must have an extension.");
        }
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();

        // Create a sanitized stadium name to prevent illegal characters in the filename
        String sanitizedStadiumName = stadiumName.replaceAll("[^a-zA-Z0-9_-]", "_");

        // Generate the new file name
        String fileName = sanitizedStadiumName + "_" + ownerId + "_" + type + fileExtension;

        // Construct the file path
        Path filePath = Paths.get(uploadDir, fileName);

        // Ensure the directories exist
        if (!Files.exists(filePath.getParent())) {
            Files.createDirectories(filePath.getParent());
        }

        // Save the file
        Files.write(filePath, image.getBytes());

        // Return the relative URL
        return "/uploads/" + fileName;
    }



    @PostMapping("/owner/add")
    public ResponseEntity<Stadium> addStadium(
            @RequestPart("stadium") String stadiumJson, // Use String here for raw JSON
            @RequestPart("mainImage") MultipartFile mainImage,
            @RequestPart(value = "additionalImages", required = false) List<MultipartFile> additionalImages,
            HttpServletRequest request) {
        try {
            // Parse JSON string to Stadium object
            Stadium stadium = new ObjectMapper().readValue(stadiumJson, Stadium.class);

            // Validate the user
            String token = authenticationService.extractToken(request);
            User user = authenticationService.extractUserFromToken(token);
            validateLoggedInOwner(user);

            // Validate main image
            if (mainImage == null || mainImage.isEmpty()) {
                throw new IllegalArgumentException("Main image is required.");
            }

            // Upload main image with custom name
            String mainImageUrl = saveImageWithCustomName(mainImage, stadium.getName(), user.getId(), "main");
            List<String> additionalImageUrls = new ArrayList<>();
            if (additionalImages != null) {
                for (int i = 0; i < additionalImages.size(); i++) {
                    MultipartFile image = additionalImages.get(i);
                    if (!image.isEmpty()) {
                        additionalImageUrls.add(saveImageWithCustomName(image, stadium.getName(), user.getId(), "additional_" + (i + 1)));
                    }
                }
            }


            // Set Stadium properties
            stadium.setMainImage(mainImageUrl);
            stadium.setAdditionalImages(additionalImageUrls);
            stadium.setOwnerId(user.getId());

            // Save stadium
            Stadium savedStadium = stadiumService.addStadium(stadium, user.getId());

            return ResponseEntity.ok(savedStadium);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }



    // Owner fetches all their stadiums
    @GetMapping("/owner/all")
    public ResponseEntity<List<Stadium>> getAllStadiums(HttpServletRequest request) {
        String token = authenticationService.extractToken(request);
        User owner = authenticationService.extractUserFromToken(token);
        validateLoggedInOwner(owner);
        List<Stadium> stadiums = stadiumService.getStadiumsByOwner(owner.getId());
        return ResponseEntity.ok(stadiums);
    }

    // Owner searches their stadiums
    @GetMapping("/owner/search")
    public ResponseEntity<List<Stadium>> searchStadiums(
            @RequestParam("query") String query, HttpServletRequest request) {
        String token = authenticationService.extractToken(request);
        User owner = authenticationService.extractUserFromToken(token);
        validateLoggedInOwner(owner);
        List<Stadium> stadiums = stadiumService.searchStadiums(owner.getId(), query);
        return ResponseEntity.ok(stadiums);
    }

    // Admin approves a stadium
    @PutMapping("/admin/approve/{stadiumId}")
    public ResponseEntity<Stadium> approveStadium(
            @PathVariable String stadiumId, HttpServletRequest request) {
        String token = authenticationService.extractToken(request);
        User admin = authenticationService.extractUserFromToken(token);
        validateLoggedInAdmin(admin);
        Stadium approvedStadium = stadiumService.approveStadium(stadiumId, admin.getId());
        return ResponseEntity.ok(approvedStadium);
    }

    // Admin cancels a stadium
    @PutMapping("/admin/cancel/{stadiumId}")
    public ResponseEntity<Stadium> cancelStadium(
            @PathVariable String stadiumId, HttpServletRequest request) {
        String token = authenticationService.extractToken(request);
        User admin = authenticationService.extractUserFromToken(token);
        validateLoggedInAdmin(admin);
        Stadium canceledStadium = stadiumService.cancelStadium(stadiumId, admin.getId());
        return ResponseEntity.ok(canceledStadium);
    }

    // Admin fetches all stadiums
    @GetMapping("/admin/all")
    public ResponseEntity<List<Stadium>> getAllStadiumsForAdmin(HttpServletRequest request) {
        String token = authenticationService.extractToken(request);
        User admin = authenticationService.extractUserFromToken(token);
        validateLoggedInAdmin(admin);
        List<Stadium> stadiums = stadiumService.getAllStadiums();
        return ResponseEntity.ok(stadiums);
    }

    // Get stadium details by ID
    @GetMapping("/{stadiumId}")
    public ResponseEntity<Stadium> getStadiumById(@PathVariable String stadiumId) {
        Stadium stadium = stadiumService.getStadiumById(stadiumId);
        return ResponseEntity.ok(stadium);
    }

    // Owner deletes a stadium
    @DeleteMapping("/owner/delete/{stadiumId}")
    public ResponseEntity<String> deleteStadium(
            @PathVariable String stadiumId, HttpServletRequest request) {
        String token = authenticationService.extractToken(request);
        User owner = authenticationService.extractUserFromToken(token);
        validateLoggedInOwner(owner);
        stadiumService.deleteStadium(stadiumId, owner.getId());
        return ResponseEntity.ok("Stadium deleted successfully");
    }
}