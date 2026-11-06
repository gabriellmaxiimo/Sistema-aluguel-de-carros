package br.pucminas.aluguel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Automovel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String matricula;

    @NotNull
    private Integer ano;

    @NotBlank
    private String marca;

    @NotBlank
    private String modelo;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String placa;

    protected Automovel() {}

    public Automovel(String matricula, Integer ano, String marca, String modelo, String placa) {
        this.matricula = matricula;
        this.ano = ano;
        this.marca = marca;
        this.modelo = modelo;
        this.placa = placa;
    }

    public Long getId() { return id; }
    public String getMatricula() { return matricula; }
    public Integer getAno() { return ano; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public String getPlaca() { return placa; }
}
