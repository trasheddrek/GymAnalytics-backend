package com.gym.analytics.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe proporcionar un email válido")
    String email,

    @NotBlank(message = "La contraseña es obligatoria")
    String password
    
) {}