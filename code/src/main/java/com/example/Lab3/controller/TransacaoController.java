package com.example.Lab3.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Lab3.model.Aluno;
import com.example.Lab3.model.Professor;
import com.example.Lab3.model.Transacao;
import com.example.Lab3.repository.AlunoRepository;
import com.example.Lab3.repository.ProfessorRepository;
import com.example.Lab3.repository.TransacaoRepository;
import com.example.Lab3.repository.VantagemRepository;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final TransacaoRepository transacaoRepository;
    private final ProfessorRepository professorRepository;
    private final AlunoRepository alunoRepository;
    private final JavaMailSender mailSender;

    public TransacaoController(TransacaoRepository transacaoRepository,
            ProfessorRepository professorRepository,
            AlunoRepository alunoRepository,
            VantagemRepository vantagemRepository,
            JavaMailSender mailSender) {
        this.transacaoRepository = transacaoRepository;
        this.professorRepository = professorRepository;
        this.alunoRepository = alunoRepository;
        this.mailSender = mailSender;
    }

    @PostMapping("/enviar-moedas")
    public ResponseEntity<?> enviarMoedas(@RequestBody Transacao transacao) {
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

        // Verificar saldo suficiente
        if (professor.getSaldoMoedas() < transacao.getQuantidade()) {
            return ResponseEntity.badRequest().body("Saldo insuficiente");
        }

        // Atualizar saldos
        professor.setSaldoMoedas(professor.getSaldoMoedas() - transacao.getQuantidade());
        aluno.setSaldoMoedas(aluno.getSaldoMoedas() + transacao.getQuantidade());

        // Configurar transação
        transacao.setData(LocalDateTime.now());
        transacao.setProfessor(professor);
        transacao.setAluno(aluno);

        // Salvar transação e atualizar saldos
        Transacao novaTransacao = transacaoRepository.save(transacao);
        professorRepository.save(professor);
        alunoRepository.save(aluno);

        // Enviar email de notificação (implementação direta)
        enviarEmailNotificacao(aluno, professor, transacao);

        return ResponseEntity.ok(novaTransacao);
    }

    private void enviarEmailNotificacao(Aluno aluno, Professor professor, Transacao transacao) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(aluno.getEmail());
        email.setSubject("Você recebeu moedas!");
        email.setText(String.format(
                "Olá %s,\n\nVocê recebeu %d moedas do professor %s.\nMotivo: %s",
                aluno.getNome(),
                transacao.getQuantidade(),
                professor.getNome(),
                transacao.getMotivo()
        ));
        mailSender.send(email);
    }
}
