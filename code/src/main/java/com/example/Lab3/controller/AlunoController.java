package com.example.Lab3.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Lab3.model.Aluno;
import com.example.Lab3.model.Empresa;
import com.example.Lab3.model.Transacao;
import com.example.Lab3.model.Vantagem;
import com.example.Lab3.repository.AlunoRepository;
import com.example.Lab3.repository.TransacaoRepository;
import com.example.Lab3.repository.VantagemRepository;
import com.example.Lab3.service.EmailService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoRepository alunoRepository;
    private final VantagemRepository vantagemRepository;
    private final TransacaoRepository transacaoRepository;
    private final JavaMailSender mailSender;
    private final EmailService emailService;

    public AlunoController(AlunoRepository alunoRepository,
            VantagemRepository vantagemRepository,
            TransacaoRepository transacaoRepository,
            JavaMailSender mailSender) {
        this(alunoRepository, vantagemRepository, transacaoRepository, mailSender, null);
    }

    public AlunoController(AlunoRepository alunoRepository,
            VantagemRepository vantagemRepository,
            TransacaoRepository transacaoRepository,
            JavaMailSender mailSender,
            EmailService emailService) {
        this.alunoRepository = alunoRepository;
        this.vantagemRepository = vantagemRepository;
        this.transacaoRepository = transacaoRepository;
        this.mailSender = mailSender;
        this.emailService = emailService;
    }

    @GetMapping
    public List<Aluno> listar() {
        return alunoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluno> buscarPorId(@PathVariable Long id) {
        Optional<Aluno> alunoOpt = alunoRepository.findById(id);
        return alunoOpt
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Aluno criar(@RequestBody Aluno aluno) {
        return alunoRepository.save(aluno);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aluno> atualizar(
            @PathVariable Long id,
            @RequestBody Aluno dados) {

        return alunoRepository.findById(id)
                .map(alunoExistente -> {
                    alunoExistente.setNome(dados.getNome());
                    alunoExistente.setEmail(dados.getEmail());
                    alunoExistente.setCpf(dados.getCpf());
                    alunoExistente.setRg(dados.getRg());

                    if (dados.getSenha() != null && !dados.getSenha().isEmpty()) {
                        alunoExistente.setSenha(dados.getSenha());
                    }

                    alunoExistente.setCurso(dados.getCurso());
                    alunoExistente.setRua(dados.getRua());
                    alunoExistente.setNumero(dados.getNumero());
                    alunoExistente.setBairro(dados.getBairro());
                    alunoExistente.setCep(dados.getCep());
                    alunoExistente.setInstituicao(dados.getInstituicao());
                    alunoExistente.setSaldoMoedas(dados.getSaldoMoedas());

                    Aluno atualizado = alunoRepository.save(alunoExistente);
                    return ResponseEntity.ok(atualizado);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (alunoRepository.existsById(id)) {
            alunoRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{alunoId}/trocar-vantagem/{vantagemId}")
    @Transactional
    public ResponseEntity<?> trocarVantagem(
            @PathVariable Long alunoId,
            @PathVariable Long vantagemId) {

        Optional<Aluno> alunoOpt = alunoRepository.findById(alunoId);
        Optional<Vantagem> vantagemOpt = vantagemRepository.findById(vantagemId);

        if (!alunoOpt.isPresent() || !vantagemOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Aluno aluno = alunoOpt.get();
        Vantagem vantagem = vantagemOpt.get();
        Empresa empresa = vantagem.getEmpresa();

        if (aluno.getSaldoMoedas() < vantagem.getCustoMoedas()) {
            return ResponseEntity.badRequest().body("Saldo insuficiente");
        }

        aluno.setSaldoMoedas(aluno.getSaldoMoedas() - vantagem.getCustoMoedas());
        alunoRepository.save(aluno);

        Transacao transacao = new Transacao();
        transacao.setAluno(aluno);
        transacao.setEmpresa(empresa);
        transacao.setVantagem(vantagem);
        transacao.setQuantidade(vantagem.getCustoMoedas());
        transacao.setData(LocalDateTime.now());
        transacao.setMotivo("Resgate: " + vantagem.getNome());
        transacao.setTipoTransacao("RESGATE");

        Transacao transacaoSalva = transacaoRepository.save(transacao);

        emailService.enviarCupomEmail(aluno, empresa, vantagem, transacaoSalva.getCodigo(), transacaoSalva.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("mensagem", "Vantagem resgatada com sucesso. Cupom enviado por email.");
        response.put("codigoCupom", transacaoSalva.getCodigo());
        response.put("transacaoId", transacaoSalva.getId());
        response.put("vantagem", vantagem.getNome());

        return ResponseEntity.ok(response);
    }

}
