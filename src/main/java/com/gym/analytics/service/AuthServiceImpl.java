package com.gym.analytics.service;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gym.analytics.dto.AuthResponseDto;
import com.gym.analytics.dto.CodigoRecuperarResponse;
import com.gym.analytics.dto.LoginRequestDto;
import com.gym.analytics.dto.RecuperarPasswordRequest;
import com.gym.analytics.dto.RegistrarRequestDto;
import com.gym.analytics.dto.RestablecerPasswordRequest;
import com.gym.analytics.dto.UsuarioResponseDto;
import com.gym.analytics.enums.Rol;
import com.gym.analytics.exception.EmailYaExisteException;
import com.gym.analytics.exception.UsuarioNoExisteException;
import com.gym.analytics.model.PasswordResetToken;
import com.gym.analytics.model.Usuario;
import com.gym.analytics.repository.PasswordResetTokenRepository;
import com.gym.analytics.repository.UsuarioRepository;
import com.gym.analytics.security.JwtService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public AuthServiceImpl(
        UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
        AuthenticationManager authenticationManager, JwtService jwtService,
        PasswordResetTokenRepository passwordResetTokenRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @Transactional
    @Override
    public UsuarioResponseDto registrarUsuario(RegistrarRequestDto request) {
        
        usuarioRepository.buscarUsuarioPorEmail(request.email())
                .ifPresent(usuario -> {
                    throw new EmailYaExisteException("El email ya existe");
                });

        Usuario usuarioGuardado = usuarioRepository.save(Usuario
            .builder()
                .nombre(request.nombre())
                .appat(request.appat())
                .apmat(request.apmat())
                .fechaNacimiento(request.fechaNacimiento())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .rol(Rol.USUARIO)
                .sexo(request.sexo())
            .build());

        return UsuarioResponseDto
            .builder()
                .nombre(usuarioGuardado.getNombre())
                .appat(usuarioGuardado.getAppat())
                .apmat(usuarioGuardado.getApmat())
                .fechaNacimiento(usuarioGuardado.getFechaNacimiento())
                .email(usuarioGuardado.getEmail())
                .nombreRol(usuarioGuardado.getRol().name())
                .sexo(usuarioGuardado.getSexo())
            .build();
        }

    @Transactional(readOnly = true)
    @Override
    public AuthResponseDto loginUsuario(LoginRequestDto request) {

        Authentication authentication =
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String token = jwtService.generarToken(userDetails);

        return new AuthResponseDto(token);
    }

    @Transactional(readOnly = true)
    @Override
    public UsuarioResponseDto obtener() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long id = Long.valueOf(authentication.getName());

        return usuarioRepository.buscarUsuarioPorId(id)
                .map(usuario -> UsuarioResponseDto.builder()
                        .id(usuario.getId())
                        .nombre(usuario.getNombre())
                        .appat(usuario.getAppat())
                        .apmat(usuario.getApmat())
                        .fechaNacimiento(usuario.getFechaNacimiento())
                        .email(usuario.getEmail())
                        .nombreRol(usuario.getRol().name())
                        .sexo(usuario.getSexo())
                        .build())
                .orElseThrow(() ->
                        new UsuarioNoExisteException("Usuario no encontrado"));
    }

    @Transactional
    @Override
    public CodigoRecuperarResponse recuperarPassword(RecuperarPasswordRequest request) {

        Usuario usuario = usuarioRepository.buscarUsuarioPorEmail(request.email())
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        String codigo = String.valueOf(
                ThreadLocalRandom.current().nextInt(100000, 1000000)
        );

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(codigo)
                .expiracion(LocalDateTime.now().plusMinutes(10))
                .usuario(usuario)
                .build();

        PasswordResetToken tokenGuardado = passwordResetTokenRepository.save(resetToken);

        return new CodigoRecuperarResponse(tokenGuardado.getToken());
    }

    @Transactional
    @Override
    public void restablecerPassword(RestablecerPasswordRequest request) {

        PasswordResetToken resetToken =
                passwordResetTokenRepository.encontrarPorToken(request.codigo())
                        .orElseThrow(() ->
                                new RuntimeException("Código inválido"));

        if (resetToken.getExpiracion().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("El código ha expirado");
        }

        Usuario usuario = resetToken.getUsuario();

        if (!usuario.getEmail().equals(request.email())) {
            throw new RuntimeException("El código no corresponde al usuario");
        }

        usuario.setPassword(
                passwordEncoder.encode(request.nuevaPassword())
        );

        usuarioRepository.save(usuario);

        passwordResetTokenRepository.delete(resetToken);
    }
}