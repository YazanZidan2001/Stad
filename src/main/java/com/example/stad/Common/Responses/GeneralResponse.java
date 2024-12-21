package com.example.stad.Common.Responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeneralResponse {
    @JsonProperty
    private String message;

    @JsonProperty
    private Boolean success;

    @JsonProperty
    private List<String> errors;

    public GeneralResponse(String message, Boolean success) {
        this.message = message;
        this.success = success;
    }

    public GeneralResponse( String message) {
    }
}
