package com.example.stad.Core.Services;

import com.example.stad.Common.DTOs.LoginDTO;
import com.example.stad.Common.Entities.Token;
import com.example.stad.Common.Entities.User;
import com.example.stad.Common.Enums.Role;
import com.example.stad.Core.Repositories.TokenRepository;
import com.example.stad.Core.Repositories.UserRepository;
import com.example.stad.WebApi.Security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private  final TokenRepository tokenRepository;
    private final TokenService tokenService;


    public Token login(LoginDTO loginDTO) {
        // Fetch user from the database
        User user = userRepository.findByEmailAddress(loginDTO.getContactInfo())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate password
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Revoke all existing tokens for the user
        tokenRepository.findAll().stream()
                .filter(t -> t.getUser().equals(user))
                .forEach(t -> {
                    t.setRevoked(true);
                    tokenRepository.save(t);
                });

        // Generate and save a new token
        String jwtToken = jwtTokenUtil.generateToken(user);
        return tokenService.saveToken(jwtToken, user);
    }


    public void registerOwner(User user) {
        // Check if the user already exists
        if (userRepository.findByEmailAddress(user.getEmailAddress()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // Set default role and encode password
        user.setRoles(List.of(Role.OWNER)); // Default role is CUSTOMER
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save user to the database
        userRepository.save(user);
    }

    public void registerCustomer(User user) {
        // Check if the user already exists
        if (userRepository.findByEmailAddress(user.getEmailAddress()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // Set default role and encode password
        user.setRoles(List.of(Role.CUSTOMER)); // Default role is CUSTOMER
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save user to the database
        userRepository.save(user);
    }

    public void registerAdmin(User user) {
        // Check if the user already exists
        if (userRepository.findByEmailAddress(user.getEmailAddress()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // Set default role and encode password
        user.setRoles(List.of(Role.ADMIN)); // Default role is CUSTOMER
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save user to the database
        userRepository.save(user);
    }

    public User extractUserFromToken(String token) {
        String username = jwtTokenUtil.extractUsername(token);
        return userRepository.findByEmailAddress(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
