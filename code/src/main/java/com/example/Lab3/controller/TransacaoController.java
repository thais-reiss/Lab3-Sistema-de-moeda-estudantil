package com.example.Lab3.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Lab3.dto.TransacaoDTO;
import com.example.Lab3.model.Aluno;
import com.example.Lab3.model.Professor;
import com.example.Lab3.model.Transacao;
import com.example.Lab3.repository.AlunoRepository;
import com.example.Lab3.repository.ProfessorRepository;
import com.example.Lab3.repository.TransacaoRepository;
import com.example.Lab3.repository.VantagemRepository;
import com.example.Lab3.service.EmailService;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final TransacaoRepository transacaoRepository;
    private final ProfessorRepository professorRepository;
    private final AlunoRepository alunoRepository;
    private final JavaMailSender mailSender;
    private final VantagemRepository vantagemRepository;
    private final EmailService emailService;

    public TransacaoController(TransacaoRepository transacaoRepository,
            ProfessorRepository professorRepository,
            AlunoRepository alunoRepository,
            VantagemRepository vantagemRepository,
            JavaMailSender mailSender,
            EmailService emailService) {
        this.transacaoRepository = transacaoRepository;
        this.professorRepository = professorRepository;
        this.alunoRepository = alunoRepository;
        this.mailSender = mailSender;
        this.vantagemRepository = vantagemRepository;
        this.emailService = emailService;
    }

    @PostMapping("/enviar-moedas")
    @Transactional
    public ResponseEntity<?> enviarMoedas(@RequestBody Transacao transacao) {
        if (transacao.getProfessor() == null || transacao.getProfessor().getId() == null) {
            return ResponseEntity.badRequest().body("ID do professor inválido");
        }
        if (transacao.getAluno() == null || transacao.getAluno().getId() == null) {
            return ResponseEntity.badRequest().body("ID do aluno inválido");
        }
        // Validar professor
        Professor professor = professorRepository.findById(transacao.getProfessor().getId()).orElse(null);
        if (professor == null) {
            return ResponseEntity.badRequest().body("Professor não encontrado");
        }

        // Validar aluno
        Aluno aluno = alunoRepository.findById(transacao.getAluno().getId()).orElse(null);
        if (aluno == null) {
            return ResponseEntity.badRequest().body("Aluno não encontrado");
        }

        int result = professorRepository.debitarSaldo(professor.getId(), transacao.getQuantidade());
        if (result == 0) {
            return ResponseEntity.badRequest().body("Saldo insuficiente");
        }

        // Operação atômica de crédito
        alunoRepository.creditarSaldo(aluno.getId(), transacao.getQuantidade());

        Transacao novaTransacao = new Transacao();
        novaTransacao.setQuantidade(transacao.getQuantidade());
        novaTransacao.setMotivo(transacao.getMotivo());
        novaTransacao.setTipoTransacao("ENVIO");
        novaTransacao.setData(LocalDateTime.now());
        novaTransacao.setProfessor(professor);
        novaTransacao.setAluno(aluno);

        transacaoRepository.save(novaTransacao);

        // Enviar email de notificação (implementação direta)
        emailService.enviarNotificacaoEnvioMoedas(aluno, professor, novaTransacao);

        return ResponseEntity.ok(novaTransacao);
    }

    @Value("${spring.mail.from}")
    private String remetenteEmail;

    @GetMapping
    public List<TransacaoDTO> listarTransacoes() {
        return transacaoRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private TransacaoDTO toDto(Transacao tx) {
        TransacaoDTO dto = new TransacaoDTO();
        dto.setId(tx.getId());
        dto.setData(tx.getData());
        dto.setQuantidade(tx.getQuantidade());
        dto.setMotivo(tx.getMotivo());
        dto.setTipoTransacao(tx.getTipoTransacao());

        if (tx.getProfessor() != null) {
            dto.setProfessorNome(tx.getProfessor().getNome());
        }
        if (tx.getAluno() != null) {
            dto.setAlunoNome(tx.getAluno().getNome());
        }
        if (tx.getEmpresa() != null) {
            dto.setEmpresaNome(tx.getEmpresa().getNome());
        }
        if (tx.getVantagem() != null) {
            dto.setVantagemNome(tx.getVantagem().getNome());
            dto.setVantagemFotoUrl(tx.getVantagem().getFotoUrl());
        }
        if (tx.getCodigo() != null) {
            dto.setCodigo(tx.getCodigo());
        }

        return dto;
    }

    @GetMapping("/aluno/{alunoId}")
    public List<TransacaoDTO> getTransacoesPorAluno(@PathVariable Long alunoId) {
        return transacaoRepository.findByAlunoId(alunoId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
