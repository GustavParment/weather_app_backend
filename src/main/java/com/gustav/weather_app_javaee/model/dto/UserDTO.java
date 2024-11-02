package com.gustav.weather_app_javaee.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gustav.weather_app_javaee.authorities.UserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    @NotBlank(message = "Username is required")
    @JsonProperty("username")
    private String username;

    @NotBlank(message ="Password is required")
    @JsonProperty("password")
    private String password;

    @JsonProperty("role")
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
