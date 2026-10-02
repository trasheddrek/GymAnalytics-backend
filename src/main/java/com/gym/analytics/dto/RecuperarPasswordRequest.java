package com.gym.analytics.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RecuperarPasswordRequest(
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe proporcionar un email válido")
    String email

) {}
