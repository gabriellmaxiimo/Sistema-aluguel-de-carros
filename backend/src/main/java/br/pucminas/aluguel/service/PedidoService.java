package br.pucminas.aluguel.service;

import br.pucminas.aluguel.dto.AvaliacaoRequest;
import br.pucminas.aluguel.dto.ContratoRequest;
import br.pucminas.aluguel.dto.PedidoRequest;
import br.pucminas.aluguel.model.*;
import br.pucminas.aluguel.repository.AutomovelRepository;
import br.pucminas.aluguel.repository.PedidoAluguelRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Gestão de pedidos e contratos.
 * Fluxo: PENDENTE -> (avaliação financeira do agente) APROVADO | REPROVADO -> (execução) CONTRATADO.
 * O cliente pode cancelar enquanto o pedido estiver PENDENTE ou APROVADO.
 */
@Service
public class PedidoService {
    private final PedidoAluguelRepository pedidos;
    private final AutomovelRepository automoveis;

    public PedidoService(PedidoAluguelRepository pedidos, AutomovelRepository automoveis) {
        this.pedidos = pedidos;
        this.automoveis = automoveis;
    }

    @Transactional
    public PedidoAluguel criar(Cliente cliente, PedidoRequest req) {
        PedidoAluguel p = new PedidoAluguel();
        p.setCliente(cliente);
        preencher(p, req);
        return pedidos.save(p);
    }

    /** Cliente consulta os próprios pedidos; agente consulta todos. */
    public List<PedidoAluguel> listar(Usuario usuario) {
        return usuario instanceof Cliente c ? pedidos.findByClienteOrderByIdDesc(c) : pedidos.findAllByOrderByIdDesc();
    }

    public PedidoAluguel buscar(Long id, Usuario usuario) {
        PedidoAluguel p = pedidos.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
        if (usuario instanceof Cliente && !p.getCliente().getId().equals(usuario.getId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado");
        return p;
    }

    /** Cliente modifica apenas pedidos pendentes; agente pode modificar pendentes ou aprovados. */
    @Transactional
    public PedidoAluguel modificar(Long id, PedidoRequest req, Usuario usuario) {
        PedidoAluguel p = buscar(id, usuario);
        boolean permitido = p.getStatus() == StatusPedido.PENDENTE
                || (usuario instanceof Agente && p.getStatus() == StatusPedido.APROVADO);
        if (!permitido) throw conflito("Pedido não pode ser modificado no status " + p.getStatus());
        preencher(p, req);
        return pedidos.save(p);
    }

    @Transactional
    public PedidoAluguel cancelar(Long id, Cliente cliente) {
        PedidoAluguel p = buscar(id, cliente);
        if (p.getStatus() != StatusPedido.PENDENTE && p.getStatus() != StatusPedido.APROVADO)
            throw conflito("Pedido não pode ser cancelado no status " + p.getStatus());
        p.setStatus(StatusPedido.CANCELADO);
        return pedidos.save(p);
    }

    @Transactional
    public PedidoAluguel avaliar(Long id, AvaliacaoRequest req, Agente agente) {
        PedidoAluguel p = buscar(id, agente);
        if (p.getStatus() != StatusPedido.PENDENTE) throw conflito("Apenas pedidos pendentes podem ser avaliados");
        p.setStatus(req.aprovado() ? StatusPedido.APROVADO : StatusPedido.REPROVADO);
        p.setParecer(req.parecer());
        p.setAgenteAvaliador(agente);
        return pedidos.save(p);
    }

    @Transactional
    public PedidoAluguel executarContrato(Long id, ContratoRequest req, Agente agente) {
        PedidoAluguel p = buscar(id, agente);
        if (p.getStatus() != StatusPedido.APROVADO) throw conflito("Apenas pedidos aprovados podem ser contratados");
        ContratoCredito credito = null;
        if (req.credito() != null) {
            if (agente.getTipo() != TipoAgente.BANCO)
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas bancos concedem contrato de crédito");
            credito = new ContratoCredito(agente, req.credito().valor(), req.credito().taxaJuros(),
                    req.credito().numeroParcelas());
        }
        p.setContrato(new Contrato(req.tipoPropriedade(), credito));
        p.setStatus(StatusPedido.CONTRATADO);
        return pedidos.save(p);
    }

    private void preencher(PedidoAluguel p, PedidoRequest req) {
        if (req.dataFim().isBefore(req.dataInicio()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data final anterior à data inicial");
        p.setAutomovel(automoveis.findById(req.automovelId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Automóvel não encontrado")));
        p.setDataInicio(req.dataInicio());
        p.setDataFim(req.dataFim());
    }

    private static ResponseStatusException conflito(String msg) {
        return new ResponseStatusException(HttpStatus.CONFLICT, msg);
    }
}
