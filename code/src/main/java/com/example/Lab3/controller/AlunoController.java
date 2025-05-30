package com.example.Lab3.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Lab3.model.Aluno;
import com.example.Lab3.repository.AlunoRepository;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoRepository alunoRepository;

    public AlunoController(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @GetMapping
    public List<Aluno> listar() {
        return alunoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluno> buscarPorId(@PathVariable Long id) {
        Optional<Aluno> alunoOpt = alunoRepository.findById(id);
        return alunoOpt
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Aluno criar(@RequestBody Aluno aluno) {
        return alunoRepository.save(aluno);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aluno> atualizar(
            @PathVariable Long id,
            @RequestBody Aluno dados) {

        return alunoRepository.findById(id)
                .map(alunoExistente -> {
                    alunoExistente.setNome(dados.getNome());
                    alunoExistente.setEmail(dados.getEmail());
                    alunoExistente.setCpf(dados.getCpf());
                    alunoExistente.setRg(dados.getRg());

                    if (dados.getSenha() != null && !dados.getSenha().isEmpty()) {
                        alunoExistente.setSenha(dados.getSenha());
                    }

                    alunoExistente.setCurso(dados.getCurso());
                    alunoExistente.setRua(dados.getRua());
                    alunoExistente.setNumero(dados.getNumero());
                    alunoExistente.setBairro(dados.getBairro());
                    alunoExistente.setCep(dados.getCep());
                    alunoExistente.setInstituicao(dados.getInstituicao());
                    alunoExistente.setSaldoMoedas(dados.getSaldoMoedas());

                    Aluno atualizado = alunoRepository.save(alunoExistente);
                    return ResponseEntity.ok(atualizado);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (alunoRepository.existsById(id)) {
            alunoRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
