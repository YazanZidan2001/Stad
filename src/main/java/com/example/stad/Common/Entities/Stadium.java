package com.example.stad.Common.Entities;

import com.example.stad.Common.Enums.StadiumStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "stadiums")
public class Stadium {

    @Id
    private String id;

    @NotNull(message = "Stadium name cannot be null")
    @NotEmpty(message = "Stadium name cannot be empty")
    private String name;

    @NotNull(message = "Location cannot be null")
    @NotEmpty(message = "Location cannot be empty")
    private String location;

    @NotNull(message = "Hourly price cannot be null")
    private double hourlyPrice;

    private double length;

    private double width;

    private boolean hasLighting;

    private boolean hasBalls;

    private String mainImage; // URL for the main image

    private List<String> additionalImages; // List of additional image URLs

    @Size(max = 500, message = "Remarks cannot exceed 500 characters")
    private String remarks; // Additional remarks or description of the stadium

    @NotNull(message = "Number of players cannot be null")
    @Positive(message = "Number of players must be positive")
    private Integer numberOfPlayers; // Maximum number of players allowed

    @NotNull
    private StadiumStatus status; // StadiumStatus (PENDING, APPROVED, REJECTED)

    @NotNull
    private String ownerId; // Reference to the User (OWNER)


    private String adminId; // Reference to the admin who approved/rejected the stadium


}