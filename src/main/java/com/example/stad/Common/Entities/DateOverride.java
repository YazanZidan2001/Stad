package com.example.stad.Common.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "date_overrides")
public class DateOverride {

    @Id
    private String id;

    private String stadiumId; // Reference to the stadium

    private String date;      // Specific date for override

    private String startTime; // Override opening time (null if closed)

    private String endTime;   // Override closing time (null if closed)

    private boolean isClosed; // Flag indicating if the stadium is closed
}
