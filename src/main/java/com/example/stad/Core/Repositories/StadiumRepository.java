package com.example.stad.Core.Repositories;

import com.example.stad.Common.Entities.Stadium;
import com.example.stad.Common.Enums.StadiumStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StadiumRepository extends MongoRepository<Stadium, String> {

    // Find all stadiums by owner ID
    List<Stadium> findByOwnerId(String ownerId);

    // Find stadiums by status
    List<Stadium> findByStatus(StadiumStatus status);

    // Search stadiums by owner ID and name
    List<Stadium> findByOwnerIdAndNameContainingIgnoreCase(String ownerId, String name);

    @Query("{$or: [ " +
            "{ 'name': { $regex: ?0, $options: 'i' } }, " +
            "{ 'location': { $regex: ?0, $options: 'i' } }, " +
            "{ 'ownerId': { $regex: ?0, $options: 'i' } }, " +
            "{ 'remarks': { $regex: ?0, $options: 'i' } }, " +
            "{ 'status': { $regex: ?0, $options: 'i' } }" +
            "]}")
    List<Stadium> searchStadiums(String search);
}
