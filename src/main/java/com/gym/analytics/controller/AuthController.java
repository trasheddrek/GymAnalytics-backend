package com.gym.analytics.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gym.analytics.dto.AuthResponseDto;
import com.gym.analytics.dto.CodigoRecuperarResponse;
import com.gym.analytics.dto.LoginRequestDto;
import com.gym.analytics.dto.RecuperarPasswordRequest;
import com.gym.analytics.dto.RegistrarRequestDto;
import com.gym.analytics.dto.RestablecerPasswordRequest;
import com.gym.analytics.dto.UsuarioResponseDto;
import com.gym.analytics.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/obtener")
    public ResponseEntity<UsuarioResponseDto> obtener() {

        UsuarioResponseDto response = authService.obtener();

        return ResponseEntity.ok().body(response);
    }


    @PostMapping("/registrar")
    public ResponseEntity<UsuarioResponseDto> register(@RequestBody RegistrarRequestDto request) {

        UsuarioResponseDto response = authService.registrarUsuario(request);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto request) {
        AuthResponseDto response = authService.loginUsuario(request);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/recuperar")
    public ResponseEntity<CodigoRecuperarResponse> recuperarPassword(@RequestBody RecuperarPasswordRequest request) {

        CodigoRecuperarResponse response = authService.recuperarPassword(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/restablecer")
    public void restablecerPassword(@RequestBody RestablecerPasswordRequest request) {

        authService.restablecerPassword(request);

    }

}