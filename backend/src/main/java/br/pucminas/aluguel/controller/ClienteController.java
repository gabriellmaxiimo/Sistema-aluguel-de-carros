package br.pucminas.aluguel.controller;

import br.pucminas.aluguel.dto.ClienteRequest;
import br.pucminas.aluguel.dto.ClienteResponse;
import br.pucminas.aluguel.service.ClienteService;
import br.pucminas.aluguel.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/** CRUD de cliente. O cadastro (POST) é público; demais operações exigem login. */
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    private final ClienteService clientes;
    private final UsuarioService usuarios;

    public ClienteController(ClienteService clientes, UsuarioService usuarios) {
        this.clientes = clientes;
        this.usuarios = usuarios;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse criar(@Valid @RequestBody ClienteRequest req) {
        return ClienteResponse.from(clientes.criar(req));
    }

    @GetMapping
    public List<ClienteResponse> listar() {
        return clientes.listar().stream().map(ClienteResponse::from).toList();
    }

    @GetMapping("/{id}")
    public ClienteResponse buscar(@PathVariable Long id, Principal principal) {
        return ClienteResponse.from(clientes.buscar(id, usuarios.atual(principal)));
    }

    @PutMapping("/{id}")
    public ClienteResponse atualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequest req, Principal principal) {
        return ClienteResponse.from(clientes.atualizar(id, req, usuarios.atual(principal)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id, Principal principal) {
        clientes.excluir(id, usuarios.atual(principal));
    }
}
