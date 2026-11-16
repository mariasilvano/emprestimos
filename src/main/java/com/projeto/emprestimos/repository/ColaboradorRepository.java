package com.projeto.emprestimos.repository;

import com.projeto.emprestimos.model.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {

    boolean existsByEmail(String email);
}
