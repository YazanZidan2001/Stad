package com.example.stad.Common.Entities;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "reservations")
public class Reservation {

    @Id
    private String id;

    @NotNull
    private String stadiumId;

    @NotNull
    private String userId;

    @NotNull
    private LocalDate date;

    @NotNull
    private String startTime; // e.g., "10:00"

    @NotNull
    private String endTime;   // e.g., "11:00"

    private boolean canceled;
}
