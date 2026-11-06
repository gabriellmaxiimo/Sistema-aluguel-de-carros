package br.pucminas.aluguel.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Cliente extends Usuario {
    @Column(nullable = false)
    private String rg;

    @Column(nullable = false, unique = true)
    private String cpf;

    private String endereco;
    private String profissao;

    /** Entidades empregadoras e rendimentos auferidos (máximo 3). */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id")
    private List<Rendimento> rendimentos = new ArrayList<>();

    @Override
    public Perfil getPerfil() { return Perfil.CLIENTE; }

    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getProfissao() { return profissao; }
    public void setProfissao(String profissao) { this.profissao = profissao; }
    public List<Rendimento> getRendimentos() { return rendimentos; }
}
