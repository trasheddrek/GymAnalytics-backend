package com.gym.analytics.service;

import com.gym.analytics.dto.AuthResponseDto;
import com.gym.analytics.dto.CodigoRecuperarResponse;
import com.gym.analytics.dto.LoginRequestDto;
import com.gym.analytics.dto.RecuperarPasswordRequest;
import com.gym.analytics.dto.RegistrarRequestDto;
import com.gym.analytics.dto.RestablecerPasswordRequest;
import com.gym.analytics.dto.UsuarioResponseDto;

public interface AuthService {

    UsuarioResponseDto registrarUsuario(RegistrarRequestDto request);

    AuthResponseDto loginUsuario(LoginRequestDto request);

    UsuarioResponseDto obtener();

    CodigoRecuperarResponse recuperarPassword(RecuperarPasswordRequest request);

    void restablecerPassword(RestablecerPasswordRequest request);

}
