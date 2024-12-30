package com.example.stad.WebApi.Controller.Owner;

import com.example.stad.Common.Entities.DateOverride;
import com.example.stad.Common.Entities.Schedule;
import com.example.stad.Common.Entities.User;
import com.example.stad.Core.Services.AuthenticationService;
import com.example.stad.Core.Services.ReservationService;
import com.example.stad.Core.Services.StadiumScheduleService;
import com.example.stad.SessionManagement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/owner")
@RequiredArgsConstructor
public class StadiumScheduleController extends SessionManagement {

    private final StadiumScheduleService scheduleService;
    private final AuthenticationService authenticationService;
    private final ReservationService reservationService;

    // Add or update recurring schedule
    @PostMapping("/add-schedule")
    public ResponseEntity<Schedule> addSchedule(@RequestBody Schedule schedule, HttpServletRequest request) {
        String token = authenticationService.extractToken(request);
        User user = authenticationService.extractUserFromToken(token);
        validateLoggedInOwner(user);
        Schedule savedSchedule = scheduleService.addOrUpdateSchedule(schedule);
        return ResponseEntity.ok(savedSchedule);
    }

    // Add or update a specific date override
    @PostMapping("/add-override")
    public ResponseEntity<DateOverride> addDateOverride(@RequestBody DateOverride override, HttpServletRequest request) {
        String token = authenticationService.extractToken(request);
        User user = authenticationService.extractUserFromToken(token);
        validateLoggedInOwner(user);
        DateOverride savedOverride = scheduleService.addOrUpdateDateOverride(override);
        return ResponseEntity.ok(savedOverride);
    }

    // Fetch schedules for a stadium
    @GetMapping("/stadium/schedules/{stadiumId}")
    public ResponseEntity<List<Schedule>> getSchedules(@PathVariable String stadiumId, HttpServletRequest request) {
        String token = authenticationService.extractToken(request);
        User user = authenticationService.extractUserFromToken(token);
        validateLoggedInAllUser(user);
        List<Schedule> schedules = scheduleService.getSchedulesForStadium(stadiumId);
        return ResponseEntity.ok(schedules);
    }

    // Fetch date overrides for a stadium
    @GetMapping("/stadium/overrides/{stadiumId}")
    public ResponseEntity<List<DateOverride>> getDateOverrides(@PathVariable String stadiumId, HttpServletRequest request) {
        String token = authenticationService.extractToken(request);
        User user = authenticationService.extractUserFromToken(token);
        validateLoggedInOwner(user);
        List<DateOverride> overrides = scheduleService.getOverridesForStadium(stadiumId);
        return ResponseEntity.ok(overrides);
    }

    // Delete a recurring schedule
    @DeleteMapping("/delete-schedule/{scheduleId}")
    public ResponseEntity<String> deleteSchedule(@PathVariable String scheduleId, HttpServletRequest request) {
        String token = authenticationService.extractToken(request);
        User user = authenticationService.extractUserFromToken(token);
        validateLoggedInOwner(user);
        scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.ok("Schedule deleted successfully");
    }

    // Delete a date override
    @DeleteMapping("/delete-override/{overrideId}")
    public ResponseEntity<String> deleteOverride(@PathVariable String overrideId, HttpServletRequest request) {
        String token = authenticationService.extractToken(request);
        User user = authenticationService.extractUserFromToken(token);
        validateLoggedInOwner(user);
        scheduleService.deleteDateOverride(overrideId);
        return ResponseEntity.ok("Override deleted successfully");
    }

    @GetMapping("/stadium/availability/{stadiumId}")
    public ResponseEntity<List<String>> getHourlyAvailability(@PathVariable String stadiumId, @RequestParam LocalDate date) {
        List<String> availability = reservationService.getHourlyAvailability(stadiumId, date);
        return ResponseEntity.ok(availability);
    }
}

