package com.example.Lab3.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Aluno extends Usuario {

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false, length = 11)
    private String cpf;

    @Column(nullable = false)
    private String rg;

    @Column(nullable = false)
    private String rua;

    @Column(nullable = false)
    private int numero;

    @Column(nullable = false)
    private String bairro;

    @Column(nullable = false)
    private String cep;

    @Override
    public String logar(String email, String senha) {
        return this.getEmail().equals(email) && this.getSenha().equals(senha)
                ? "Aluno autenticado" : "Falha na autenticação";
    }

    @ManyToOne
    @JoinColumn(name = "instituicao_id", nullable = false)
    private InstituicaoEnsino instituicao;
    private String curso;
    private int saldoMoedas;
}
