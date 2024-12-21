package com.example.stad.Common.Entities;

import com.example.stad.Common.Enums.TokenType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tokens") // MongoDB collection name
public class Token {

    @Id
    private String id;

    private String token;

    private TokenType tokenType;

    private boolean revoked;

    private boolean expired;

    @DBRef // Reference to the User who owns this token
    private User user;
}
