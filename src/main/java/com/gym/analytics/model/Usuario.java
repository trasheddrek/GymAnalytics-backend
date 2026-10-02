package com.gym.analytics.model;

import java.time.LocalDate;

import com.gym.analytics.enums.Rol;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder

public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String appat;

    @Column(nullable = true)
    private String apmat;

    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @Column (nullable = false)
    private String sexo; 
    
}
