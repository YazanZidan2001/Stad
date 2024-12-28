package com.example.stad.WebApi.Controller.Owner;


import com.example.stad.Common.Entities.Stadium;
import com.example.stad.Common.Entities.User;
import com.example.stad.Common.Enums.Role;
import com.example.stad.Core.Services.AuthenticationService;
import com.example.stad.Core.Services.StadiumService;
import com.example.stad.SessionManagement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@RestController
@RequestMapping("/stadium")
@RequiredArgsConstructor
public class StadController extends SessionManagement {

    private final StadiumService stadiumService;
    private final AuthenticationService authenticationService;


//    /**
//     * Add a new XRay (only doctors can add x-rays).
//     */
//    @PostMapping
//    public ResponseEntity<String> addXRay(@RequestBody XRay xRay, HttpServletRequest request)
//            throws UserNotFoundException, NotFoundException {
//        String token = authenticationService.extractToken(request);
//        User user = authenticationService.extractUserFromToken(token);
//        validateLoggedInDoctor(user);
//
//        xRayService.addXRay(xRay);
//        return ResponseEntity.ok("XRay added successfully");
//    }
//
//
//    // Owner adds a stadium
//    @PostMapping("/owner/add")
//    public ResponseEntity<Stadium> addStadium(
//            @RequestBody Stadium stadium,
//            @RequestPart("mainImage") MultipartFile mainImage,
//            @RequestPart("additionalImages") List<MultipartFile> additionalImages,
//            @RequestHeader("Authorization") String ownerToken,
//            HttpServletRequest request) throws IOException {
//
//        String token = authenticationService.extractToken(request);
//        User user = authenticationService.extractUserFromToken(token);
//        validateLoggedInOwner(user);
//
//
//        // Save images to the local folder and get their URLs
//        String mainImageUrl = saveImage(mainImage, "main");
//        List<String> additionalImageUrls = saveImages(additionalImages);
//
//        stadium.setMainImage(mainImageUrl);
//        stadium.setAdditionalImages(additionalImageUrls);
//        stadium.setOwnerId(owner.getId());
//
//        Stadium savedStadium = stadiumService.addStadium(stadium, owner.getId());
//        return ResponseEntity.ok(savedStadium);
//    }
//
//
//
//
//


}
