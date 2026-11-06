package br.pucminas.aluguel.dto;

import jakarta.validation.constraints.NotNull;

/** Parecer financeiro do agente sobre o pedido. */
public record AvaliacaoRequest(@NotNull Boolean aprovado, String parecer) {
}
