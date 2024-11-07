package com.gustav.weather_app_javaee.model.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gustav.weather_app_javaee.authorities.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


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
    @Size(min = 6, message = "Passowrd must be at least 6 characters long")
    @JsonProperty("password")
    private String password;

    @NotNull(message = "Roles are required")
    @JsonProperty("roles")
    private Set<Role> roles;


}
