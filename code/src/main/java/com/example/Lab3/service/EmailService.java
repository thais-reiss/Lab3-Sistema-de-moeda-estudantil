package com.example.Lab3.service;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.Lab3.model.Aluno;
import com.example.Lab3.model.Empresa;
import com.example.Lab3.model.Professor;
import com.example.Lab3.model.Transacao;
import com.example.Lab3.model.Vantagem;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarCupomEmail(Aluno aluno, Empresa empresa, Vantagem vantagem, Long codigoCupom, Long transacaoId) {
        try {
            System.out.println("Tentando enviar email para: " + aluno.getEmail());

            SimpleMailMessage emailAluno = new SimpleMailMessage();
            emailAluno.setFrom("testemoedas454@gmail.com");
            emailAluno.setTo(aluno.getEmail());
            emailAluno.setSubject("Cupom de vantagem - " + vantagem.getNome());
            emailAluno.setText(String.format(
                    "Olá %s,\n\nVocê resgatou: %s\nCódigo do cupom: %d\nID da transação: %d",
                    aluno.getNome(), vantagem.getNome(), codigoCupom, transacaoId
            ));
            mailSender.send(emailAluno);

            SimpleMailMessage emailEmpresa = new SimpleMailMessage();
            emailEmpresa.setFrom("testemoedas454@gmail.com");
            emailEmpresa.setTo(empresa.getEmail());
            emailEmpresa.setSubject("Resgate de vantagem - " + vantagem.getNome());
            emailEmpresa.setText(String.format(
                    "Olá %s,\n\nO aluno %s resgatou: %s\nCódigo do cupom: %d\nID da transação: %d",
                    empresa.getNome(), aluno.getNome(), vantagem.getNome(), codigoCupom, transacaoId
            ));
            mailSender.send(emailEmpresa);

            System.out.println("Emails de resgate enviados com sucesso.");
        } catch (MailException e) {
            System.err.println("ERRO AO ENVIAR EMAIL DE RESGATE: ");
            e.printStackTrace();
        }
    }

    public void enviarNotificacaoEnvioMoedas(Aluno aluno, Professor professor, Transacao transacao) {
        try {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom("testemoedas454@gmail.com");
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
            System.out.println("Email enviado com sucesso para " + aluno.getEmail());
        } catch (MailException ex) {
            System.err.println("Erro ao enviar e-mail: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
} 