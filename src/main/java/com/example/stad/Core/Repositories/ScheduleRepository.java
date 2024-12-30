package com.example.stad.Core.Repositories;

import com.example.stad.Common.Entities.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends MongoRepository<Schedule, String> {
    List<Schedule> findByStadiumId(String stadiumId);
}
