package com.gym.analytics.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)

@Builder
public record UsuarioResponseDto (   

    Long id,
    String nombre,
    String appat,
    String apmat,
    LocalDate fechaNacimiento,
    String email,
    String nombreRol,
    String sexo

) {}
