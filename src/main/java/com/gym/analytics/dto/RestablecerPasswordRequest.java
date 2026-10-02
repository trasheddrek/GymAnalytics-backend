package com.gym.analytics.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RestablecerPasswordRequest(

    String email,

    String codigo,

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 10, max = 50, message = "La contraseña debe tener entre 10 y 50 caracteres")
    @Pattern(
        regexp = "^(?!\\s)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?<!\\s).*$",
        message = "La contraseña debe contener al menos una mayúscula, una minúscula y un carácter especial, sin espacios al inicio o final"
    )
    String nuevaPassword
) {}