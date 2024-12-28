package com.example.stad.WebApi.Controller.Owner;

import com.example.stad.Common.Entities.Stadium;
import com.example.stad.Common.Entities.User;
import com.example.stad.Common.Enums.Role;
import com.example.stad.Core.Services.AuthenticationService;
import com.example.stad.Core.Services.StadiumService;
import com.example.stad.SessionManagement;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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

    @Value("${stadium.images.base-path}")
    private String folderPath;

    @PostConstruct
    public void ensureDirectoryExists() {
        File folder = new File(folderPath);
        if (!folder.exists() && !folder.mkdirs()) {
            throw new RuntimeException("Failed to create directory: " + folderPath);
        }
    }

    @PostMapping("/owner/add")
    public ResponseEntity<Stadium> addStadium(
            @RequestPart("stadium") Stadium stadium,
            @RequestPart("mainImage") MultipartFile mainImage,
            @RequestPart(value = "additionalImages", required = false) List<MultipartFile> additionalImages,
            HttpServletRequest request) throws IOException {

        // Debugging Logs
        System.out.println("Content-Type: " + request.getContentType());
        System.out.println("Stadium Data: " + stadium);
        System.out.println("Main Image Name: " + mainImage.getOriginalFilename());
        if (additionalImages != null) {
            System.out.println("Additional Images Count: " + additionalImages.size());
        }

        // Extract User and Validate Ownership
        String token = authenticationService.extractToken(request);
        User user = authenticationService.extractUserFromToken(token);
        validateLoggedInOwner(user);

        // Validate images
        if (mainImage == null || mainImage.isEmpty()) {
            throw new IllegalArgumentException("Main image is required.");
        }

        // Save images and get URLs
        String mainImageUrl = saveImage(mainImage, "main");
        List<String> additionalImageUrls = saveImages(additionalImages);

        // Set Stadium Properties
        stadium.setMainImage(mainImageUrl);
        stadium.setAdditionalImages(additionalImageUrls);
        stadium.setOwnerId(user.getId());

        // Save Stadium
        Stadium savedStadium = stadiumService.addStadium(stadium, user.getId());
        return ResponseEntity.ok(savedStadium);
    }

    private String saveImage(MultipartFile image, String type) throws IOException {
        String fileName = type + "_" + UUID.randomUUID() + "_" + image.getOriginalFilename();
        File file = new File(folderPath + fileName);

        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            throw new IOException("Failed to create directory for image storage.");
        }

        image.transferTo(file);
        return "/images/stadiums/" + fileName;
    }

    private List<String> saveImages(List<MultipartFile> images) throws IOException {
        List<String> imageUrls = new ArrayList<>();
        if (images != null) {
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    imageUrls.add(saveImage(image, "additional"));
                }
            }
        }
        return imageUrls;
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