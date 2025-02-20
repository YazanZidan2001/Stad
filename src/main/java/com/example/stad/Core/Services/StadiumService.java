package com.example.stad.Core.Services;

import com.example.stad.Common.DTOs.StadiumWithOwnerDTO;
import com.example.stad.Common.Entities.Reservation;
import com.example.stad.Common.Entities.Stadium;
import com.example.stad.Common.Entities.User;
import com.example.stad.Common.Enums.StadiumStatus;
import com.example.stad.Core.Repositories.StadiumRepository;
import com.example.stad.Core.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StadiumService {

    private final StadiumRepository stadiumRepository;
    private List<Reservation> reservations = new ArrayList<>();
    private final UserRepository userRepository; // Inject UserRepository

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

    // Fetch PENDING stadiums
    public List<Stadium> getPendingStadiums() {
        return stadiumRepository.findByStatus(StadiumStatus.PENDING);
    }

    // Fetch APPROVED stadiums
    public List<Stadium> getApprovedStadiums() {
        return stadiumRepository.findByStatus(StadiumStatus.APPROVED);
    }

    // Fetch REJECTED stadiums
    public List<Stadium> getRejectedStadiums() {
        return stadiumRepository.findByStatus(StadiumStatus.REJECTED);
    }

    // Fetch stadium details with owner info
    public StadiumWithOwnerDTO getStadiumWithOwnerDTO(String stadiumId) {
        Stadium stadium = stadiumRepository.findById(stadiumId)
                .orElseThrow(() -> new RuntimeException("Stadium not found"));

        User owner = userRepository.findById(stadium.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        return new StadiumWithOwnerDTO(stadium, owner);
    }

    public User getOwnerByStadiumId(String stadiumId) {
        // Fetch the stadium by ID
        Stadium stadium = stadiumRepository.findById(stadiumId)
                .orElseThrow(() -> new RuntimeException("Stadium not found"));

        // Fetch the owner by the stadium's ownerId
        return userRepository.findById(stadium.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));
    }


    public User getCustomerById(String userId) {
        // Fetch user by ID
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }


}
