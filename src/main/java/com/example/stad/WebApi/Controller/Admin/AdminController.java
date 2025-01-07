package com.example.stad.WebApi.Controller.Admin;

import com.example.stad.Common.DTOs.StadiumWithOwnerDTO;
import com.example.stad.Common.Entities.Schedule;
import com.example.stad.Common.Entities.Stadium;
import com.example.stad.Common.Entities.User;
import com.example.stad.Core.Services.AuthenticationService;
import com.example.stad.Core.Services.ReservationService;
import com.example.stad.Core.Services.StadiumScheduleService;
import com.example.stad.Core.Services.StadiumService;
import com.example.stad.SessionManagement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController extends SessionManagement {

    private final StadiumService stadiumService;
    private final AuthenticationService authenticationService;
    private  final StadiumScheduleService scheduleService;
    private final ReservationService reservationService;



    // Admin approves a stadium
    @PutMapping("/approve/{stadiumId}")
    public ResponseEntity<Stadium> approveStadium(
            @PathVariable String stadiumId, HttpServletRequest request) {
        String token = authenticationService.extractToken(request);
        User admin = authenticationService.extractUserFromToken(token);
        validateLoggedInAdmin(admin);
        Stadium approvedStadium = stadiumService.approveStadium(stadiumId, admin.getId());
        return ResponseEntity.ok(approvedStadium);
    }

    // Admin cancels a stadium
    @PutMapping("/cancel/{stadiumId}")
    public ResponseEntity<Stadium> cancelStadium(
            @PathVariable String stadiumId, HttpServletRequest request) {
        String token = authenticationService.extractToken(request);
        User admin = authenticationService.extractUserFromToken(token);
        validateLoggedInAdmin(admin);
        Stadium canceledStadium = stadiumService.cancelStadium(stadiumId, admin.getId());
        return ResponseEntity.ok(canceledStadium);
    }

    // Get stadium details by ID
    @GetMapping("/get-stadium/{stadiumId}")
    public ResponseEntity<Stadium> getStadiumById(@PathVariable String stadiumId,HttpServletRequest request) {
        String token = authenticationService.extractToken(request);
        User admin = authenticationService.extractUserFromToken(token);
        validateLoggedInAllUser(admin);
        Stadium stadium = stadiumService.getStadiumById(stadiumId);
        return ResponseEntity.ok(stadium);
    }

    // Admin fetches all stadiums
    @GetMapping("/all-stadiums")
    public ResponseEntity<List<Stadium>> getAllStadiumsForAdmin(HttpServletRequest request) {
        String token = authenticationService.extractToken(request);
        User admin = authenticationService.extractUserFromToken(token);
        validateLoggedInAdmin(admin);
        List<Stadium> stadiums = stadiumService.getAllStadiums();
        return ResponseEntity.ok(stadiums);
    }

    // Fetch schedules for a stadium
    @GetMapping("/get-stadium-schedule/{stadiumId}")
    public ResponseEntity<List<Schedule>> getSchedules(@PathVariable String stadiumId, HttpServletRequest request) {
        String token = authenticationService.extractToken(request);
        User admin = authenticationService.extractUserFromToken(token);
        validateLoggedInAllUser(admin);
        List<Schedule> schedules = scheduleService.getSchedulesForStadium(stadiumId);
        return ResponseEntity.ok(schedules);
    }

    // Fetch PENDING stadiums
    @GetMapping("/stadiums/pending")
    public ResponseEntity<List<Stadium>> getPendingStadiums() {
        List<Stadium> stadiums = stadiumService.getPendingStadiums();
        return ResponseEntity.ok(stadiums);
    }

    // Fetch APPROVED stadiums
    @GetMapping("/stadiums/approved")
    public ResponseEntity<List<Stadium>> getApprovedStadiums() {
        List<Stadium> stadiums = stadiumService.getApprovedStadiums();
        return ResponseEntity.ok(stadiums);
    }

    // Fetch REJECTED stadiums
    @GetMapping("/stadiums/rejected")
    public ResponseEntity<List<Stadium>> getRejectedStadiums() {
        List<Stadium> stadiums = stadiumService.getRejectedStadiums();
        return ResponseEntity.ok(stadiums);
    }

    // Fetch stadium details with owner info
    @GetMapping("/stadiums/{stadiumId}")
    public ResponseEntity<StadiumWithOwnerDTO> getStadiumWithOwner(@PathVariable String stadiumId) {
        StadiumWithOwnerDTO stadiumWithOwner = stadiumService.getStadiumWithOwnerDTO(stadiumId);
        return ResponseEntity.ok(stadiumWithOwner);
    }
}
