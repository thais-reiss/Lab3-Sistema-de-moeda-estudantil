package com.example.Lab3.controller;

import com.example.Lab3.model.Empresa;
import com.example.Lab3.repository.EmpresaRepository;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    private final EmpresaRepository empresaRepository;

    public EmpresaController(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @GetMapping
    public List<Empresa> listar() {
        return empresaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empresa> buscarPorId(@PathVariable Long id) {
        Optional<Empresa> empresaOpt = empresaRepository.findById(id);
        return empresaOpt
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Empresa criar(@RequestBody Empresa empresa) {
        return empresaRepository.save(empresa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empresa> atualizar(
            @PathVariable Long id,
            @RequestBody Empresa dados) {

        return empresaRepository.findById(id)
                .map(empresaExistente -> {
                    empresaExistente.setEmail(dados.getEmail());
                    empresaExistente.setSenha(dados.getSenha());
                    empresaExistente.setNome(dados.getNome());
                    empresaExistente.setCnpj(dados.getCnpj());
                    Empresa atualizada = empresaRepository.save(empresaExistente);
                    return ResponseEntity.ok(atualizada);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (empresaRepository.existsById(id)) {
            empresaRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}