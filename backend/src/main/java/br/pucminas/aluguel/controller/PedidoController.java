package br.pucminas.aluguel.controller;

import br.pucminas.aluguel.dto.AvaliacaoRequest;
import br.pucminas.aluguel.dto.ContratoRequest;
import br.pucminas.aluguel.dto.PedidoRequest;
import br.pucminas.aluguel.dto.PedidoResponse;
import br.pucminas.aluguel.model.Agente;
import br.pucminas.aluguel.model.Cliente;
import br.pucminas.aluguel.service.PedidoService;
import br.pucminas.aluguel.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/** Perfis exigidos por rota estão em SecurityConfig. */
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    private final PedidoService pedidos;
    private final UsuarioService usuarios;

    public PedidoController(PedidoService pedidos, UsuarioService usuarios) {
        this.pedidos = pedidos;
        this.usuarios = usuarios;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponse criar(@Valid @RequestBody PedidoRequest req, Principal principal) {
        return PedidoResponse.from(pedidos.criar((Cliente) usuarios.atual(principal), req));
    }

    @GetMapping
    public List<PedidoResponse> listar(Principal principal) {
        return pedidos.listar(usuarios.atual(principal)).stream().map(PedidoResponse::from).toList();
    }

    @GetMapping("/{id}")
    public PedidoResponse buscar(@PathVariable Long id, Principal principal) {
        return PedidoResponse.from(pedidos.buscar(id, usuarios.atual(principal)));
    }

    @PutMapping("/{id}")
    public PedidoResponse modificar(@PathVariable Long id, @Valid @RequestBody PedidoRequest req, Principal principal) {
        return PedidoResponse.from(pedidos.modificar(id, req, usuarios.atual(principal)));
    }

    @PostMapping("/{id}/cancelar")
    public PedidoResponse cancelar(@PathVariable Long id, Principal principal) {
        return PedidoResponse.from(pedidos.cancelar(id, (Cliente) usuarios.atual(principal)));
    }

    @PostMapping("/{id}/avaliacao")
    public PedidoResponse avaliar(@PathVariable Long id, @Valid @RequestBody AvaliacaoRequest req, Principal principal) {
        return PedidoResponse.from(pedidos.avaliar(id, req, (Agente) usuarios.atual(principal)));
    }

    @PostMapping("/{id}/contrato")
    public PedidoResponse executarContrato(@PathVariable Long id, @Valid @RequestBody ContratoRequest req, Principal principal) {
        return PedidoResponse.from(pedidos.executarContrato(id, req, (Agente) usuarios.atual(principal)));
    }
}
