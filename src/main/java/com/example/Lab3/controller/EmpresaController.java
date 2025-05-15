package com.example.Lab3.controller;

import com.example.Lab3.model.Empresa;
import com.example.Lab3.repository.EmpresaRepository;
import java.util.List;
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

    @PostMapping
    public Empresa criar(@RequestBody Empresa empresa) {
        return empresaRepository.save(empresa);
    }
}