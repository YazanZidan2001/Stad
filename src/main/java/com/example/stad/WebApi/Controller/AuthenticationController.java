package com.example.stad.WebApi.Controller;

import com.example.stad.Common.Entities.Token;
import com.example.stad.Common.Entities.User;
import com.example.stad.Common.DTOs.LoginDTO;
import com.example.stad.Common.Enums.Role;
import com.example.stad.Common.Responses.AuthenticationResponse;
import com.example.stad.Core.Services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*") // Allow the frontend origin
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginDTO loginDTO) {
        AuthenticationResponse response = authenticationService.login(loginDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register-customer")
    public ResponseEntity<AuthenticationResponse> registerCustomer(@RequestBody User user) {
        AuthenticationResponse response = authenticationService.register(user, Role.CUSTOMER);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register-owner")
    public ResponseEntity<AuthenticationResponse> registerOwner(@RequestBody User user) {
        AuthenticationResponse response = authenticationService.register(user, Role.OWNER);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register-admin")
    public ResponseEntity<AuthenticationResponse> registerAdmin(@RequestBody User user) {
        AuthenticationResponse response = authenticationService.register(user, Role.ADMIN);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getUser")
    public ResponseEntity<User> getUser(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        User user = authenticationService.extractUserFromToken(token);
        return ResponseEntity.ok(user);
    }
}
