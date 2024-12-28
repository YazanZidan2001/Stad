package com.example.stad.Common.Entities;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bookings")
public class Booking {

    @Id
    private String id;

    @NotNull(message = "User ID cannot be null")
    private String userId; // Reference to the User (CUSTOMER)

    @NotNull(message = "Stadium ID cannot be null")
    private String stadiumId; // Reference to the Stadium

    @NotNull(message = "Slot ID cannot be null")
    private String slotId; // Reference to the BookingSlot

    @NotNull(message = "Total price cannot be null")
    private double totalPrice;

    @NotNull(message = "Booking date cannot be null")
    private LocalDate date;

    @NotNull(message = "Start time cannot be null")
    private LocalTime startTime;

    @NotNull(message = "End time cannot be null")
    private LocalTime endTime;
}
