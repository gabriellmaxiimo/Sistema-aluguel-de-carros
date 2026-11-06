package br.pucminas.aluguel.dto;

import br.pucminas.aluguel.model.TipoPropriedade;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/** Execução do contrato. "credito" é opcional e só pode ser concedido por banco. */
public record ContratoRequest(@NotNull TipoPropriedade tipoPropriedade, @Valid CreditoDTO credito) {

    public record CreditoDTO(@NotNull @DecimalMin("0.01") BigDecimal valor,
                             @NotNull @DecimalMin("0") BigDecimal taxaJuros,
                             @NotNull @Min(1) Integer numeroParcelas) {}
}
