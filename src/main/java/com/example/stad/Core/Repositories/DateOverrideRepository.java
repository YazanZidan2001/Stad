package com.example.stad.Core.Repositories;

import com.example.stad.Common.Entities.DateOverride;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DateOverrideRepository extends MongoRepository<DateOverride, String> {
    List<DateOverride> findByStadiumId(String stadiumId);
}
