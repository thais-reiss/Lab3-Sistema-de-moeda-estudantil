package com.example.Lab3.controller;

import com.example.Lab3.model.Professor;
import com.example.Lab3.repository.ProfessorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/professores")
public class ProfessorController {

    private final ProfessorRepository professorRepository;

    public ProfessorController(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    @GetMapping
    public List<Professor> listar() {
        return professorRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Professor> buscarPorId(@PathVariable Long id) {
        Optional<Professor> professorOpt = professorRepository.findById(id);
        return professorOpt
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Professor criar(@RequestBody Professor professor) {
        return professorRepository.save(professor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Professor> atualizar(
            @PathVariable Long id,
            @RequestBody Professor dados) {

        return professorRepository.findById(id)
                .map(professorExistente -> {
                    professorExistente.setNome(dados.getNome());
                    professorExistente.setEmail(dados.getEmail());
                    professorExistente.setCpf(dados.getCpf());
                    professorExistente.setDepartamento(dados.getDepartamento());

                    if (dados.getSenha() != null && !dados.getSenha().isEmpty()) {
                        professorExistente.setSenha(dados.getSenha());
                    }

                    professorExistente.setInstituicao(dados.getInstituicao());
                    professorExistente.setSaldoMoedas(dados.getSaldoMoedas());

                    Professor atualizado = professorRepository.save(professorExistente);
                    return ResponseEntity.ok(atualizado);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (professorRepository.existsById(id)) {
            professorRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
