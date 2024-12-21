package com.example.stad.Core.Services;

import com.example.stad.Common.Entities.Token;
import com.example.stad.Common.Entities.User;
import com.example.stad.Common.Enums.TokenType;
import com.example.stad.Core.Repositories.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    // Save a new token
    public Token saveToken(String jwtToken, User user) {
        Token token = Token.builder()
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .user(user)
                .build();

        return tokenRepository.save(token);
    }

    // Revoke a token
    public void revokeToken(String token) {
        tokenRepository.findByToken(token).ifPresent(t -> {
            t.setRevoked(true);
            tokenRepository.save(t);
        });
    }

    // Extract user from token
    public Optional<User> extractUserFromToken(String token) {
        return tokenRepository.findByToken(token).map(Token::getUser);
    }
}
