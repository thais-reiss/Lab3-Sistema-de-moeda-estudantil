package com.example.Lab3.controller;

import com.example.Lab3.model.Transacao;
import com.example.Lab3.repository.TransacaoRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/extratos")
public class ExtratoController {

    private final TransacaoRepository transacaoRepository;

    public ExtratoController(TransacaoRepository transacaoRepository) {
        this.transacaoRepository = transacaoRepository;
    }

    @GetMapping("/professor/{professorId}")
    public List<Transacao> extratoProfessor(@PathVariable Long professorId) {
        return transacaoRepository.findByProfessorId(professorId);
    }

    @GetMapping("/aluno/{alunoId}")
    public List<Transacao> extratoAluno(@PathVariable Long alunoId) {
        return transacaoRepository.findByAlunoId(alunoId);
    }
}
