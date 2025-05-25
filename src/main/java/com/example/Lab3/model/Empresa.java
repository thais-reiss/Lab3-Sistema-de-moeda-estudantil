package com.example.Lab3.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Empresa extends Usuario {

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false, length = 14)
    private String cnpj;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Vantagem> vantagens;

    @Override
    public String logar(String email, String senha) {
        return this.getEmail().equals(email) && this.getSenha().equals(senha)
                ? "Empresa autenticado"
                : "Falha na autenticação";
    }
}
