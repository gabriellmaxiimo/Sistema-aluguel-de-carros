package br.pucminas.aluguel.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class PedidoAluguel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Cliente cliente;

    @ManyToOne(optional = false)
    private Automovel automovel;

    @Column(nullable = false)
    private LocalDate dataInicio;

    @Column(nullable = false)
    private LocalDate dataFim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status = StatusPedido.PENDENTE;

    private String parecer;

    @ManyToOne
    private Agente agenteAvaliador;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Contrato contrato;

    private LocalDateTime criadoEm = LocalDateTime.now();

    public Long getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Automovel getAutomovel() { return automovel; }
    public void setAutomovel(Automovel automovel) { this.automovel = automovel; }
    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }
    public StatusPedido getStatus() { return status; }
    public void setStatus(StatusPedido status) { this.status = status; }
    public String getParecer() { return parecer; }
    public void setParecer(String parecer) { this.parecer = parecer; }
    public Agente getAgenteAvaliador() { return agenteAvaliador; }
    public void setAgenteAvaliador(Agente agenteAvaliador) { this.agenteAvaliador = agenteAvaliador; }
    public Contrato getContrato() { return contrato; }
    public void setContrato(Contrato contrato) { this.contrato = contrato; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
}
