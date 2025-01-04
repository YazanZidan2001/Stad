package com.example.stad.Core.Services;

import com.example.stad.Common.Entities.Reservation;
import com.example.stad.Common.Entities.Stadium;
import com.example.stad.Common.Enums.StadiumStatus;
import com.example.stad.Core.Repositories.StadiumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StadiumService {

    private final StadiumRepository stadiumRepository;
    private List<Reservation> reservations = new ArrayList<>();

    // Owner adds a new stadium
    public Stadium addStadium(Stadium stadium, String ownerId) {
        stadium.setOwnerId(ownerId);
        stadium.setStatus(StadiumStatus.PENDING); // Default status is PENDING
        return stadiumRepository.save(stadium);
    }

    // Owner fetches all their stadiums
    public List<Stadium> getStadiumsByOwner(String ownerId) {
        return stadiumRepository.findByOwnerId(ownerId);
    }

    // Owner searches their stadiums by name
    public List<Stadium> searchStadiums(String ownerId, String query) {
        return stadiumRepository.findByOwnerIdAndNameContainingIgnoreCase(ownerId, query);
    }

    // Admin approves a stadium
    public Stadium approveStadium(String stadiumId, String adminId) {
        Stadium stadium = stadiumRepository.findById(stadiumId)
                .orElseThrow(() -> new RuntimeException("Stadium not found"));

        stadium.setStatus(StadiumStatus.APPROVED);
        stadium.setAdminId(adminId);
        return stadiumRepository.save(stadium);
    }

    // Admin cancels a stadium
    public Stadium cancelStadium(String stadiumId, String adminId) {
        Stadium stadium = stadiumRepository.findById(stadiumId)
                .orElseThrow(() -> new RuntimeException("Stadium not found"));

        stadium.setStatus(StadiumStatus.REJECTED);
        stadium.setAdminId(adminId);
        return stadiumRepository.save(stadium);
    }

    // Get stadium by ID (owner or admin use case)
    public Stadium getStadiumById(String stadiumId) {
        return stadiumRepository.findById(stadiumId)
                .orElseThrow(() -> new RuntimeException("Stadium not found"));
    }

    // Fetch all stadiums (Admin use case)
    public List<Stadium> getAllStadiums() {
        return stadiumRepository.findAll();
    }

    // Delete stadium (Owner use case)
    public void deleteStadium(String stadiumId, String ownerId) {
        Stadium stadium = stadiumRepository.findById(stadiumId)
                .orElseThrow(() -> new RuntimeException("Stadium not found"));

        if (!stadium.getOwnerId().equals(ownerId)) {
            throw new RuntimeException("You are not authorized to delete this stadium");
        }

        stadiumRepository.delete(stadium);
    }


    public List<Stadium> searchStadiums(String search) {
        if (search == null || search.isEmpty()) {
            // Return all stadiums if no search query is provided
            return stadiumRepository.findAll();
        }
        return stadiumRepository.searchStadiums(search);
    }


    public Stadium save(Stadium stadium) {
        return stadiumRepository.save(stadium);
    }

}
