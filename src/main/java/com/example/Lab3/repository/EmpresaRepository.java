package com.example.Lab3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Lab3.model.Empresa;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
}
