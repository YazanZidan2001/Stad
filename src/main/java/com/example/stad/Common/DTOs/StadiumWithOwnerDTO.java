package com.example.stad.Common.DTOs;

import com.example.stad.Common.Entities.Stadium;
import com.example.stad.Common.Entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StadiumWithOwnerDTO {
    private Stadium stadium;
    private User owner;
}

