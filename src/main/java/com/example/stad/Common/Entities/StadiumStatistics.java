package com.example.stad.Common.Entities;

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
@Document(collection = "stadium_statistics")
public class StadiumStatistics {

    @Id
    private String id;

    @NotNull
    private String stadiumId;

    private int totalBookings;

    private double totalRevenue;
}

