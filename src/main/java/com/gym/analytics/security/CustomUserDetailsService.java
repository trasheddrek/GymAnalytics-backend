package com.gym.analytics.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gym.analytics.model.Usuario;
import com.gym.analytics.repository.UsuarioRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Usuario usuario = usuarioRepository.buscarUsuarioPorEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return User.builder()
                .username(usuario.getId().toString())
                .password(usuario.getPassword())
                .roles(usuario.getRol().name())
                .build();
    }

    public UserDetails loadUserById(Long id) {

        Usuario usuario = usuarioRepository.buscarUsuarioPorId(id)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuario no encontrado"));

        return User.builder()
                .username(usuario.getId().toString())
                .password(usuario.getPassword())
                .roles(usuario.getRol().name())
                .build();
    }
}