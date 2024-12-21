package com.example.stad.Core.Services;

import com.example.stad.Common.Entities.User;
import com.example.stad.Common.Enums.Role;
import com.example.stad.Core.Repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createAdminUser() {
        User adminUser = User.builder()
                .fullName("Admin User")
                .emailAddress("admin@example.com")
                .password(passwordEncoder.encode("admin123")) // Encrypt the password
                .roles(List.of(Role.ADMIN)) // Assign the ADMIN role
                .build();

        userRepository.save(adminUser);
    }
}
