package com.git.porto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;    

@Entity
@Getter
@Setter
@NoArgsConstructor

public class Orcamento {
    
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double valorEstimado;

    @Column(nullable = false)
    private String status; // em andamento, aprovado, pendente, reprovado

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    

    @ManyToOne 
    @JoinColumn (name = "servico_id", nullable = false)
    private Servico servico;

    
}
