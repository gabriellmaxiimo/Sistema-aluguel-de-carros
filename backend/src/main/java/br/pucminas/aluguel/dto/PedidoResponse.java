package br.pucminas.aluguel.dto;

import br.pucminas.aluguel.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PedidoResponse(Long id, Long clienteId, String clienteNome, Automovel automovel,
                             LocalDate dataInicio, LocalDate dataFim, StatusPedido status,
                             String parecer, String agenteAvaliador, LocalDateTime criadoEm,
                             ContratoResponse contrato) {

    public record ContratoResponse(TipoPropriedade tipoPropriedade, LocalDate dataAssinatura,
                                   String banco, BigDecimal valorCredito, BigDecimal taxaJuros,
                                   Integer numeroParcelas) {}

    public static PedidoResponse from(PedidoAluguel p) {
        return new PedidoResponse(p.getId(), p.getCliente().getId(), p.getCliente().getNome(), p.getAutomovel(),
                p.getDataInicio(), p.getDataFim(), p.getStatus(), p.getParecer(),
                p.getAgenteAvaliador() == null ? null : p.getAgenteAvaliador().getNome(),
                p.getCriadoEm(), contrato(p.getContrato()));
    }

    private static ContratoResponse contrato(Contrato c) {
        if (c == null) return null;
        ContratoCredito cc = c.getContratoCredito();
        return cc == null
                ? new ContratoResponse(c.getTipoPropriedade(), c.getDataAssinatura(), null, null, null, null)
                : new ContratoResponse(c.getTipoPropriedade(), c.getDataAssinatura(), cc.getBanco().getNome(),
                        cc.getValor(), cc.getTaxaJuros(), cc.getNumeroParcelas());
    }
}
