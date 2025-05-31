package com.example.Lab3.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Lab3.dto.LoginDTO;
import com.example.Lab3.model.Usuario;
import com.example.Lab3.repository.UsuarioRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;

    public AuthController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        Usuario usuario = usuarioRepository.findByEmail(loginDTO.getEmail());

        if (usuario == null || !usuario.getSenha().equals(loginDTO.getSenha())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Email ou senha incorretos");
        }

        String roleLogin = loginDTO.getRole();
        String roleBanco = usuario.getRole(); 
        if (roleLogin == null || !roleLogin.equalsIgnoreCase(roleBanco)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Tipo de usuário incorreto para este usuário");
        }

        return ResponseEntity.ok(usuario);
    }
}
