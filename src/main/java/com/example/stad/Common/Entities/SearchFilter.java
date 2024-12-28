package com.example.stad.Common.Entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchFilter {
    private String location;
    private double maxPrice;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
}

