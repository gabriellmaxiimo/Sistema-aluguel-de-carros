package br.pucminas.aluguel.dto;

import br.pucminas.aluguel.model.Cliente;

import java.util.List;

public record ClienteResponse(Long id, String login, String nome, String rg, String cpf,
                              String endereco, String profissao,
                              List<ClienteRequest.RendimentoDTO> rendimentos) {

    public static ClienteResponse from(Cliente c) {
        return new ClienteResponse(c.getId(), c.getLogin(), c.getNome(), c.getRg(), c.getCpf(),
                c.getEndereco(), c.getProfissao(),
                c.getRendimentos().stream()
                        .map(r -> new ClienteRequest.RendimentoDTO(r.getEntidadeEmpregadora(), r.getValor()))
                        .toList());
    }
}
