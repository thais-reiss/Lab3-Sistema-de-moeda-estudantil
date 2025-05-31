package com.example.Lab3.repository;

import com.example.Lab3.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findByProfessorId(Long professorId);

    List<Transacao> findByAlunoId(Long alunoId);
}
