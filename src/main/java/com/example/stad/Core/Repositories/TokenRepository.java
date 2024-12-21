package com.example.stad.Core.Repositories;

import com.example.stad.Common.Entities.Token;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends MongoRepository<Token, String> {

    // Find token by token string
    Optional<Token> findByToken(String token);
}
