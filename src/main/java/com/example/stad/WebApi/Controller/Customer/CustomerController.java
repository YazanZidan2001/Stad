package com.example.stad.WebApi.Controller.Customer;

import com.example.stad.Common.Entities.Reservation;
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

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController extends SessionManagement {

    private final ReservationService reservationService;
    private final AuthenticationService authenticationService;
    private final StadiumService stadiumService;
    private  final StadiumScheduleService scheduleService;

    @PostMapping("/reserve")
    public ResponseEntity<Reservation> reserveStadium(@RequestBody Reservation reservation,HttpServletRequest request) {
        String token = authenticationService.extractToken(request);
        User customer = authenticationService.extractUserFromToken(token);
        validateLoggedInCustomer(customer);
        Reservation createdReservation = reservationService.createReservation(reservation);
        return ResponseEntity.ok(createdReservation);
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getReservations(HttpServletRequest request) {
        String token = authenticationService.extractToken(request);
        User customer = authenticationService.extractUserFromToken(token);
        validateLoggedInCustomer(customer);
        List<Reservation> reservations = reservationService.getReservationsForUser(customer.getId());
        return ResponseEntity.ok(reservations);
    }

    // Get stadium details by ID
    @GetMapping("/get-stadium/{stadiumId}")
    public ResponseEntity<Stadium> getStadiumById(@PathVariable String stadiumId, HttpServletRequest request) {
        String token = authenticationService.extractToken(request);
        User admin = authenticationService.extractUserFromToken(token);
        validateLoggedInAllUser(admin);
        Stadium stadium = stadiumService.getStadiumById(stadiumId);
        return ResponseEntity.ok(stadium);
    }

    //  fetches all stadiums
    @GetMapping("/all-stadiums")
    public ResponseEntity<List<Stadium>> getAllStadiumsForAdmin(HttpServletRequest request) {
        String token = authenticationService.extractToken(request);
        User admin = authenticationService.extractUserFromToken(token);
        validateLoggedInAllUser(admin);
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
}
