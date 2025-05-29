package com.example.Lab3.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Lab3.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByEmail(String email);
}
