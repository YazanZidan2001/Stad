package com.example.stad.Common.Entities;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "stadium_reviews")
public class StadiumReview {

    @Id
    private String id;

    @NotNull(message = "Stadium ID cannot be null")
    private String stadiumId;

    @NotNull(message = "User ID cannot be null")
    private String userId;

    @NotNull(message = "Rating cannot be null")
    @Min(1) @Max(5)
    private int rating;

    private String review;
}