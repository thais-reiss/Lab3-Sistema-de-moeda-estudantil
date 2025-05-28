package com.example.Lab3.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Lab3.model.InstituicaoEnsino;
import com.example.Lab3.repository.InstituicaoEnsinoRepository;

@RestController
@RequestMapping("/instituicoes")
public class InstituicaoEnsinoController {

    private final InstituicaoEnsinoRepository instituicaoRepository;

    public InstituicaoEnsinoController(InstituicaoEnsinoRepository instituicaoRepository) {
        this.instituicaoRepository = instituicaoRepository;
    }

    @GetMapping
    public List<InstituicaoEnsino> listar() {
        return instituicaoRepository.findAll();
    }

    @PostMapping
    public InstituicaoEnsino criar(@RequestBody InstituicaoEnsino instituicao) {
        return instituicaoRepository.save(instituicao);
    }
}
