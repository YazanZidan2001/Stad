package com.example.stad.Core.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.stad.Common.Entities.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmailAddress(String emailAddress);
}