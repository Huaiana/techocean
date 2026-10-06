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

public class Mensagem {
    
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String conteudo;

    @Column
    private String resposta;

    @ManyToOne
    @JoinColumn(name = "operacao_id", nullable = false)
    private Operacao operacao;
    
}
