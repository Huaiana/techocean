package com.git.techocean.repository;

import com.git.techocean.model.Orcamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {
    List<Orcamento> findByClienteId(Long clienteId);
}
