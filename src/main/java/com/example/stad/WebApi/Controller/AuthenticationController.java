package com.example.stad.WebApi.Controller;

import com.example.stad.Common.Entities.Token;
import com.example.stad.Common.Entities.User;
import com.example.stad.Common.DTOs.LoginDTO;
import com.example.stad.Core.Services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestBody LoginDTO loginDTO) {
        Token token = authenticationService.login(loginDTO);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register-Customer")
    public ResponseEntity<?> registerCustomer(@RequestBody User user) {
        authenticationService.registerCustomer(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/register-owner")
    public ResponseEntity<?> registerOwner(@RequestBody User user) {
        authenticationService.registerOwner(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody User user) {
        authenticationService.registerAdmin(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/getUser")
    public ResponseEntity<User> getUser(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        User user = authenticationService.extractUserFromToken(token);
        return ResponseEntity.ok(user);
    }
}
