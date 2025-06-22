package com.example.Lab3.Scheduler;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.Lab3.model.Professor;
import com.example.Lab3.model.Transacao;
import com.example.Lab3.repository.ProfessorRepository;
import com.example.Lab3.repository.TransacaoRepository;

@Component
public class MoedasScheduler {

    private final ProfessorRepository professorRepository;
    private final TransacaoRepository transacaoRepository;

    public MoedasScheduler(ProfessorRepository professorRepository, TransacaoRepository transacaoRepository) {
        this.professorRepository = professorRepository;
        this.transacaoRepository = transacaoRepository;
    }

    @Scheduled(cron = "0 0 0 1 1,7 ?") // Executa todo dia 1º de Janeiro e 1º de Julho
    public void adicionarMoedasSemestrais() {
        professorRepository.findAll().forEach(professor -> {

            // Cria e salva a transação
            Transacao transacao = new Transacao();
            transacao.setProfessor(professor);
            transacao.setQuantidade(1000);
            transacao.setMotivo("Moedas semestrais");
            transacao.setTipoTransacao("MOEDAS_SEMESTRAIS");
            transacao.setData(LocalDateTime.now());

            transacaoRepository.save(transacao);
        });
    }
}
