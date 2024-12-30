package com.example.stad.Common.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "schedules")
public class Schedule {

    @Id
    private String id;

    private String stadiumId; // Reference to the stadium

    private List<String> daysOfWeek; // List of days (e.g., ["Sunday", "Tuesday", "Thursday"])

    private String startTime; // e.g., "10:00"

    private String endTime;   // e.g., "22:00"

    private String fromDate;  // Recurrence starts from this date

    private String toDate;    // Recurrence ends on this date
}
