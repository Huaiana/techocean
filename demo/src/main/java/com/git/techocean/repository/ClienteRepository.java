package com.git.techocean.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.git.techocean.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Busca cliente pelo e-mail para autenticação de login (RF02)
    Cliente findByEmail(String email);

    // Valida se já existe cliente cadastrado com o e-mail
    boolean existsByEmail(String email);
}