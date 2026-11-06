package br.pucminas.aluguel.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Contrato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPropriedade tipoPropriedade;

    private LocalDate dataAssinatura = LocalDate.now();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private ContratoCredito contratoCredito;

    protected Contrato() {}

    public Contrato(TipoPropriedade tipoPropriedade, ContratoCredito contratoCredito) {
        this.tipoPropriedade = tipoPropriedade;
        this.contratoCredito = contratoCredito;
    }

    public Long getId() { return id; }
    public TipoPropriedade getTipoPropriedade() { return tipoPropriedade; }
    public LocalDate getDataAssinatura() { return dataAssinatura; }
    public ContratoCredito getContratoCredito() { return contratoCredito; }
}
