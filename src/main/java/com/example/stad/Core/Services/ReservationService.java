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

    public List<String> getHourlyAvailabilityWithDetails(String stadiumId, LocalDate date) {
        // Fetch schedules and overrides for the stadium
        List<Schedule> schedules = scheduleService.getSchedulesForStadium(stadiumId);
        List<DateOverride> overrides = scheduleService.getOverridesForStadium(stadiumId);
        List<Reservation> reservations = reservationRepository.findByStadiumIdAndDate(stadiumId, date);

        // Check if the date is covered by overrides
        DateOverride matchingOverride = overrides.stream()
                .filter(override -> override.getDate().equals(date.toString()))
                .findFirst()
                .orElse(null);

        // Handle override: If stadium is closed, return empty slots
        if (matchingOverride != null && matchingOverride.isClosed()) {
            return List.of("Stadium is closed on this date.");
        }

        LocalTime startTime;
        LocalTime endTime;

        if (matchingOverride != null) {
            // Use override times if available
            startTime = LocalTime.parse(matchingOverride.getStartTime());
            endTime = LocalTime.parse(matchingOverride.getEndTime());
        } else {
            // Check if the date is within any schedule's range
            Schedule matchingSchedule = schedules.stream()
                    .filter(schedule -> schedule.getDaysOfWeek().contains(date.getDayOfWeek().name()) &&
                            !date.isBefore(LocalDate.parse(schedule.getFromDate())) &&
                            !date.isAfter(LocalDate.parse(schedule.getToDate())))
                    .findFirst()
                    .orElse(null);

            if (matchingSchedule == null) {
                return List.of("No schedule found for this date.");
            }

            // Use schedule times
            startTime = LocalTime.parse(matchingSchedule.getStartTime());
            endTime = LocalTime.parse(matchingSchedule.getEndTime());
        }

        // Generate hourly slots with reservation details
        List<String> hourlySlots = new ArrayList<>();
        LocalTime currentTime = startTime;

        while (currentTime.isBefore(endTime)) {
            String slotStartTime = currentTime.toString();
            String slotEndTime = currentTime.plusHours(1).toString();
            String slot = slotStartTime + " - " + slotEndTime;

            // Find a reservation for the current slot
            Reservation reservedSlot = reservations.stream()
                    .filter(reservation -> reservation.getDate().equals(date) &&
                            reservation.getStartTime().equals(slotStartTime) &&
                            reservation.getEndTime().equals(slotEndTime) &&
                            !reservation.isCanceled())
                    .findFirst()
                    .orElse(null);

            if (reservedSlot != null) {
                hourlySlots.add(slot + " (Reserved)");
                // Add reservation details
                hourlySlots.add("    Reservation Details:");
                hourlySlots.add("        Reservation ID: " + reservedSlot.getId());
                hourlySlots.add("        User ID: " + reservedSlot.getUserId());
                hourlySlots.add("        Stadium ID: " + reservedSlot.getStadiumId());
                hourlySlots.add("        Date: " + reservedSlot.getDate());
            } else {
                hourlySlots.add(slot + " (Available)");
            }

            currentTime = currentTime.plusHours(1);
        }

        return hourlySlots;
    }


    public Reservation createReservation(Reservation reservation) {
        // Check if the requested slot is available
        List<String> availability = getHourlyAvailabilityWithDetails(reservation.getStadiumId(), reservation.getDate());
        String requestedSlot = reservation.getStartTime() + " - " + reservation.getEndTime();

        if (availability.stream().noneMatch(slot -> slot.equals(requestedSlot + " (Available)"))) {
            throw new RuntimeException("The requested time slot is unavailable");
        }

        return reservationRepository.save(reservation);
    }

    public List<Reservation> getReservationsForUser(String userId) {
        return reservationRepository.findByUserId(userId);
    }


    public void cancelReservationByCustomer(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));


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
