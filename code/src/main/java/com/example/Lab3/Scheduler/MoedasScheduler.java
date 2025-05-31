package com.example.Lab3.Scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.Lab3.repository.ProfessorRepository;

@Component
public class MoedasScheduler {

    private final ProfessorRepository professorRepository;

    public MoedasScheduler(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    @Scheduled(cron = "0 0 0 1 1,7 ?") // 1º de Janeiro e 1º de Julho
    public void adicionarMoedasSemestrais() {
        professorRepository.findAll().forEach(professor -> {
            professor.setSaldoMoedas(professor.getSaldoMoedas() + 1000);
            professorRepository.save(professor);
        });
    }
}
