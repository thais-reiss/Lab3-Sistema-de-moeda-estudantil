package com.example.Lab3.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Lab3.dto.TransacaoDTO;
import com.example.Lab3.model.Transacao;
import com.example.Lab3.repository.TransacaoRepository;

@RestController
@RequestMapping("/extratos")
public class ExtratoController {

    private final TransacaoRepository transacaoRepository;

    public ExtratoController(TransacaoRepository transacaoRepository) {
        this.transacaoRepository = transacaoRepository;
    }

    @GetMapping("/professor/{professorId}")
    public List<TransacaoDTO> extratoProfessor(@PathVariable Long professorId) {
        return transacaoRepository.findByProfessorId(professorId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/aluno/{alunoId}")
    public List<TransacaoDTO> extratoAluno(@PathVariable Long alunoId) {
        return transacaoRepository.findByAlunoId(alunoId)
                .stream()
                .map(transacao -> {
                    TransacaoDTO dto = toDto(transacao);

                    if (transacao.getEmpresa() != null) {
                        dto.setQuantidade(-dto.getQuantidade());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private TransacaoDTO toDto(Transacao transacao) {
        TransacaoDTO dto = new TransacaoDTO();
        dto.setId(transacao.getId());
        dto.setData(transacao.getData());
        dto.setQuantidade(transacao.getQuantidade());
        dto.setMotivo(transacao.getMotivo());

        if (transacao.getProfessor() != null) {
            dto.setProfessorNome(transacao.getProfessor().getNome());
        }

        if (transacao.getAluno() != null) {
            dto.setAlunoNome(transacao.getAluno().getNome());
        }

        if (transacao.getEmpresa() != null) {
            dto.setEmpresaNome(transacao.getEmpresa().getNome());
        }

        return dto;
    }
}
