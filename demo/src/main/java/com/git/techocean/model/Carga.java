package com.git.techocean.model;

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

public class Carga {
    
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private Double peso;

    // Relacionamento com Cliente (RF12)[span_2](start_span)[span_2](end_span)
    @ManyToOne 
    @JoinColumn (name = "cliente_id", nullable = false)
    private Cliente cliente;

    // Relacionamento com Operacao (RF13)[span_3](start_span)[span_3](end_span)
    @ManyToOne
    @JoinColumn (name = "operacao_id", nullable = false)
    private Operacao operacao;

    // Associar opcionalmente ao Conteiner (RF14)[span_4](start_span)[span_4](end_span)
    @ManyToOne
    @JoinColumn (name = "conteiner_id")
    private Conteiner conteiner;

}
