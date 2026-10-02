package com.gym.analytics.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gym.analytics.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query
    ("""
        SELECT u FROM Usuario u
        WHERE u.email = :email
    """)
    Optional<Usuario> buscarUsuarioPorEmail(@Param("email") String email);

    @Query
    ("""
        SELECT u FROM Usuario u
        WHERE u.id = :id
    """)
    Optional<Usuario> buscarUsuarioPorId(@Param("id") Long id);

}

