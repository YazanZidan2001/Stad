package com.example.stad.Core.Services;

import com.example.stad.Common.Entities.DateOverride;
import com.example.stad.Common.Entities.Reservation;
import com.example.stad.Common.Entities.Schedule;
import com.example.stad.Core.Repositories.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final StadiumScheduleService scheduleService;

    public List<String> getHourlyAvailability(String stadiumId, LocalDate date) {
        // Fetch schedules and overrides for the stadium
        List<Schedule> schedules = scheduleService.getSchedulesForStadium(stadiumId);
        List<DateOverride> overrides = scheduleService.getOverridesForStadium(stadiumId);
        List<Reservation> reservations = reservationRepository.findByStadiumIdAndDate(stadiumId, date);

        // Check if there is a date override for the given date
        DateOverride matchingOverride = overrides.stream()
                .filter(override -> override.getDate().equals(date.toString()))
                .findFirst()
                .orElse(null);

        // Handle override: If stadium is closed, return empty slots
        if (matchingOverride != null && matchingOverride.isClosed()) {
            return List.of("Stadium is closed on this date.");
        }

        // Use override times if available; otherwise, match the schedule for the day
        LocalTime startTime;
        LocalTime endTime;

        if (matchingOverride != null) {
            startTime = LocalTime.parse(matchingOverride.getStartTime());
            endTime = LocalTime.parse(matchingOverride.getEndTime());
        } else {
            // Match schedule for the day of the week
            Schedule matchingSchedule = schedules.stream()
                    .filter(schedule -> schedule.getDaysOfWeek().contains(date.getDayOfWeek().name()))
                    .findFirst()
                    .orElse(null);

            if (matchingSchedule == null) {
                return List.of("No schedule found for this day.");
            }

            startTime = LocalTime.parse(matchingSchedule.getStartTime());
            endTime = LocalTime.parse(matchingSchedule.getEndTime());
        }

        // Generate hourly slots
        List<String> hourlySlots = new ArrayList<>();
        LocalTime currentTime = startTime;
        while (currentTime.isBefore(endTime)) {
            String slot = currentTime.toString() + " - " + currentTime.plusHours(1).toString();
            hourlySlots.add(slot);
            currentTime = currentTime.plusHours(1);
        }

        // Mark reserved slots (exclude canceled reservations)
        List<String> reservedSlots = reservations.stream()
                .filter(reservation -> !reservation.isCanceled()) // Exclude canceled reservations
                .map(reservation -> reservation.getStartTime() + " - " + reservation.getEndTime())
                .collect(Collectors.toList());

        return hourlySlots.stream()
                .map(slot -> reservedSlots.contains(slot) ? slot + " (Reserved)" : slot + " (Available)")
                .collect(Collectors.toList());
    }



    public Reservation createReservation(Reservation reservation) {
        // Check if the requested slot is available
        List<String> availability = getHourlyAvailability(reservation.getStadiumId(), reservation.getDate());
        String requestedSlot = reservation.getStartTime() + " - " + reservation.getEndTime();

        if (availability.stream().noneMatch(slot -> slot.equals(requestedSlot + " (Available)"))) {
            throw new RuntimeException("The requested time slot is unavailable");
        }

        return reservationRepository.save(reservation);
    }

    public List<Reservation> getReservationsForUser(String userId) {
        return reservationRepository.findByUserId(userId);
    }


    public void cancelReservationByCustomer(String reservationId, String userId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (!reservation.getUserId().equals(userId)) {
            throw new RuntimeException("You are not authorized to cancel this reservation");
        }

        reservation.setCanceled(true);
        reservationRepository.save(reservation);
    }

    // Cancel reservation by owner
    public void cancelReservationByOwner(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        reservation.setCanceled(true);
        reservationRepository.save(reservation);
    }

    // Fetch all reservations for a stadium and date
    public List<Reservation> getReservationsForStadiumAndDate(String stadiumId, LocalDate date) {
        return reservationRepository.findByStadiumIdAndDate(stadiumId, date);
    }
}
