package br.pucminas.aluguel.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

/** Crédito concedido por um banco agente, associado ao contrato de aluguel. */
@Entity
public class ContratoCredito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Agente banco;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private BigDecimal taxaJuros;

    @Column(nullable = false)
    private Integer numeroParcelas;

    protected ContratoCredito() {}

    public ContratoCredito(Agente banco, BigDecimal valor, BigDecimal taxaJuros, Integer numeroParcelas) {
        this.banco = banco;
        this.valor = valor;
        this.taxaJuros = taxaJuros;
        this.numeroParcelas = numeroParcelas;
    }

    public Long getId() { return id; }
    public Agente getBanco() { return banco; }
    public BigDecimal getValor() { return valor; }
    public BigDecimal getTaxaJuros() { return taxaJuros; }
    public Integer getNumeroParcelas() { return numeroParcelas; }
}
