package br.pucminas.aluguel.model;

import jakarta.persistence.*;

/** Empresa ou banco que modifica e avalia pedidos de aluguel. */
@Entity
public class Agente extends Usuario {
    @Column(nullable = false, unique = true)
    private String cnpj;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAgente tipo;

    @Override
    public Perfil getPerfil() { return Perfil.AGENTE; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    public TipoAgente getTipo() { return tipo; }
    public void setTipo(TipoAgente tipo) { this.tipo = tipo; }
}
