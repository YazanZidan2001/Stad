package com.example.stad.Core.Services;

import com.example.stad.Common.Entities.DateOverride;
import com.example.stad.Common.Entities.Schedule;
import com.example.stad.Core.Repositories.DateOverrideRepository;
import com.example.stad.Core.Repositories.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StadiumScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final DateOverrideRepository dateOverrideRepository;

    // Add or update recurring schedule and generate actual schedule entries
    public Schedule addOrUpdateSchedule(Schedule schedule) {
        // Save the schedule
        Schedule savedSchedule = scheduleRepository.save(schedule);

        // Generate DateOverrides for the specified range and days
        generateOverridesForSchedule(savedSchedule);

        return savedSchedule;
    }

    private void generateOverridesForSchedule(Schedule schedule) {
        LocalDate fromDate = LocalDate.parse(schedule.getFromDate());
        LocalDate toDate = LocalDate.parse(schedule.getToDate());
        List<String> daysOfWeek = schedule.getDaysOfWeek();

        List<DateOverride> overrides = new ArrayList<>();
        while (!fromDate.isAfter(toDate)) {
            if (daysOfWeek.contains(fromDate.getDayOfWeek().name())) {
                DateOverride override = new DateOverride(
                        null,
                        schedule.getStadiumId(),
                        fromDate.toString(),
                        schedule.getStartTime(),
                        schedule.getEndTime(),
                        false
                );
                overrides.add(override);
            }
            fromDate = fromDate.plusDays(1);
        }

        // Log generated overrides
        System.out.println("Generated Overrides: " + overrides);

        dateOverrideRepository.saveAll(overrides);
    }



    // Add or update a specific date override
    public DateOverride addOrUpdateDateOverride(DateOverride override) {
        return dateOverrideRepository.save(override);
    }

    // Fetch overrides for a stadium
    public List<DateOverride> getOverridesForStadium(String stadiumId) {
        return dateOverrideRepository.findByStadiumId(stadiumId);
    }

    // Delete a schedule
    public void deleteSchedule(String scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }

    // Delete a date override
    public void deleteDateOverride(String overrideId) {
        dateOverrideRepository.deleteById(overrideId);
    }

    // Fetch schedules for a stadium
    public List<Schedule> getSchedulesForStadium(String stadiumId) {
        return scheduleRepository.findByStadiumId(stadiumId);
    }


}

