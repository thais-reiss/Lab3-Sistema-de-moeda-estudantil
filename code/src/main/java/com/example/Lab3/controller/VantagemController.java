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
import org.springframework.web.server.ResponseStatusException;

import com.example.Lab3.dto.VantagemDTO;
import com.example.Lab3.model.Empresa;
import com.example.Lab3.model.Vantagem;
import com.example.Lab3.repository.EmpresaRepository;
import com.example.Lab3.repository.VantagemRepository;

import jakarta.validation.Valid;

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

    @GetMapping("/{id}")
    public ResponseEntity<VantagemDTO> buscarPorId(@PathVariable Long id) {
        Optional<Vantagem> opt = vantagemRepository.findById(id);
        if (!opt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Vantagem entidade = opt.get();

        VantagemDTO dto = new VantagemDTO();
        dto.setNome(entidade.getNome());
        dto.setDescricao(entidade.getDescricao());
        dto.setFotoUrl(entidade.getFotoUrl());
        dto.setCustoMoedas(entidade.getCustoMoedas());
        dto.setEmpresaId(entidade.getEmpresa().getId());

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VantagemDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid VantagemDTO dto) {

        return vantagemRepository.findById(id).map(entidade -> {
            entidade.setNome(dto.getNome());
            entidade.setDescricao(dto.getDescricao());
            entidade.setFotoUrl(dto.getFotoUrl());
            entidade.setCustoMoedas(dto.getCustoMoedas());

            empresaRepository.findById(dto.getEmpresaId())
                    .ifPresent(entidade::setEmpresa);

            Vantagem salva = vantagemRepository.save(entidade);
            return ResponseEntity.ok(toDto(salva));
        })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return vantagemRepository.findById(id)
                .map(entidade -> {
                    vantagemRepository.delete(entidade);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private VantagemDTO toDto(Vantagem entidade) {
        VantagemDTO dto = new VantagemDTO();
        dto.setNome(entidade.getNome());
        dto.setDescricao(entidade.getDescricao());
        dto.setFotoUrl(entidade.getFotoUrl());
        dto.setCustoMoedas(entidade.getCustoMoedas());
        dto.setEmpresaId(entidade.getEmpresa().getId());
        return dto;
    }
}
