package com.example.Lab3.controller;

import com.example.Lab3.dto.VantagemDTO;
import com.example.Lab3.model.Empresa;
import com.example.Lab3.model.Vantagem;
import com.example.Lab3.repository.EmpresaRepository;
import com.example.Lab3.repository.VantagemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/vantagens")
public class VantagemController {

    private final VantagemRepository vantagemRepository;
    private final EmpresaRepository empresaRepository;

    public VantagemController(VantagemRepository vantagemRepository, EmpresaRepository empresaRepository) {
        this.vantagemRepository = vantagemRepository;
        this.empresaRepository = empresaRepository;
    }

    @PostMapping
    public ResponseEntity<Vantagem> criarVantagem(@RequestBody VantagemDTO vantagemDTO) {
        Empresa empresa = empresaRepository.findById(vantagemDTO.getEmpresaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada"));

        Vantagem vantagem = new Vantagem();
        vantagem.setNome(vantagemDTO.getNome());
        vantagem.setDescricao(vantagemDTO.getDescricao());
        vantagem.setFotoUrl(vantagemDTO.getFotoUrl());
        vantagem.setCustoMoedas(vantagemDTO.getCustoMoedas());
        vantagem.setEmpresa(empresa);

        Vantagem salva = vantagemRepository.save(vantagem);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @GetMapping
    public List<Vantagem> listarVantagens() {
        return vantagemRepository.findAll();
    }
}
