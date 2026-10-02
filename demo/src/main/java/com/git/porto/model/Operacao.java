package com.git.porto.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;    

@Entity 
@Getter
@Setter
@NoArgsConstructor

public class Operacao {
    
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column (nullable = false)
    private String descricao;

    @Column(nullable = false)
    private String tipo; // importação ou exportação

    @Column (nullable = false)
    private String status; // em andamento, aprovado, pendente, reprovado
    
}
