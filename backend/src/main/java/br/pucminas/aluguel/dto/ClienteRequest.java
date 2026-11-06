package br.pucminas.aluguel.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

/** Dados de cadastro/edição do cliente. Na edição, senha em branco mantém a atual. */
public record ClienteRequest(
        @NotBlank String login,
        String senha,
        @NotBlank String nome,
        @NotBlank String rg,
        @NotBlank String cpf,
        String endereco,
        String profissao,
        @Size(max = 3, message = "máximo de 3 rendimentos") List<@Valid RendimentoDTO> rendimentos) {

    public record RendimentoDTO(@NotBlank String entidadeEmpregadora,
                                @NotNull @DecimalMin("0") BigDecimal valor) {}
}
