package br.pucminas.aluguel.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Rendimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String entidadeEmpregadora;

    @Column(nullable = false)
    private BigDecimal valor;

    protected Rendimento() {}

    public Rendimento(String entidadeEmpregadora, BigDecimal valor) {
        this.entidadeEmpregadora = entidadeEmpregadora;
        this.valor = valor;
    }

    public Long getId() { return id; }
    public String getEntidadeEmpregadora() { return entidadeEmpregadora; }
    public BigDecimal getValor() { return valor; }
}
