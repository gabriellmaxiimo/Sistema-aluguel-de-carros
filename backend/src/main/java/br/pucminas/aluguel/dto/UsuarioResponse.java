package br.pucminas.aluguel.dto;

import br.pucminas.aluguel.model.Agente;
import br.pucminas.aluguel.model.Perfil;
import br.pucminas.aluguel.model.TipoAgente;
import br.pucminas.aluguel.model.Usuario;

public record UsuarioResponse(Long id, String login, String nome, Perfil perfil, TipoAgente tipoAgente) {

    public static UsuarioResponse from(Usuario u) {
        TipoAgente tipo = u instanceof Agente a ? a.getTipo() : null;
        return new UsuarioResponse(u.getId(), u.getLogin(), u.getNome(), u.getPerfil(), tipo);
    }
}
