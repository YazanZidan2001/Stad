package com.example.stad.WebApi.Controller.Customer;

import com.example.stad.Common.Entities.Reservation;
import com.example.stad.Common.Entities.Schedule;
import com.example.stad.Common.Entities.Stadium;
import com.example.stad.Common.Entities.User;
import com.example.stad.Core.Repositories.ReservationRepository;
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

@CrossOrigin
@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController extends SessionManagement {

    private final ReservationService reservationService;
    private final AuthenticationService authenticationService;
    private final StadiumService stadiumService;
    private  final StadiumScheduleService scheduleService;
    private final ReservationRepository reservationRepository;

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

    @PutMapping("/cancel-reservation/{reservationId}")
    public ResponseEntity<String> cancelReservation(@PathVariable String reservationId, HttpServletRequest request) {
        String token = authenticationService.extractToken(request);
        User customer = authenticationService.extractUserFromToken(token);
        validateLoggedInCustomer(customer);

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        if (!reservation.getUserId().equals(customer.getId())) {
            throw new RuntimeException("You are not authorized to cancel this reservation");
        }

        reservationService.cancelReservationByCustomer(reservationId);
        return ResponseEntity.ok("Reservation canceled successfully");
    }

    @GetMapping("/search-stadiums")
    public ResponseEntity<List<Stadium>> searchStadiums(@RequestParam(required = false) String search) {
        List<Stadium> stadiums = stadiumService.searchStadiums(search);
        return ResponseEntity.ok(stadiums);
    }
}
