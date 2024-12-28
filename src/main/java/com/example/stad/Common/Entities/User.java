package com.example.stad.Common.Entities;

import com.example.stad.Common.Enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users") // MongoDB collection
public class User {

    @Id
    private String id;

    @NotNull(message = "Full name cannot be null")
    @NotEmpty(message = "Full name cannot be empty")
    @Size(min = 2, max = 50, message = "Full name must be between 2 and 50 characters")
    private String fullName;

    @NotNull(message = "Phone number cannot be null")
    @NotEmpty(message = "Phone number cannot be empty")
    @Pattern(
            regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$",
            message = "Phone number must be valid (e.g., +1234567890 or 1234567890)"
    )
    @Indexed(unique = true) // Enforce uniqueness in MongoDB
    private String phoneNumber;

    @NotNull(message = "Email address cannot be null")
    @NotEmpty(message = "Email address cannot be empty")
    @Email(message = "Email address must be valid")
    @Indexed(unique = true) // Enforce uniqueness in MongoDB
    private String emailAddress;

    @NotNull(message = "Password cannot be null")
    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    private String password;

    @NotNull(message = "Birth date cannot be null")
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;


    @Size(max = 50, message = "City name cannot exceed 50 characters")
    private String city;

    @Size(max = 50, message = "Village name cannot exceed 50 characters")
    private String village;

    @NotNull(message = "Roles cannot be null")
    @NotEmpty(message = "Roles cannot be empty")
    private Role role; // role : (e.g., ADMIN, OWNER, CUSTOMER)
}
