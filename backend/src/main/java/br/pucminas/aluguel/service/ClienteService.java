package br.pucminas.aluguel.service;

import br.pucminas.aluguel.dto.ClienteRequest;
import br.pucminas.aluguel.model.Agente;
import br.pucminas.aluguel.model.Cliente;
import br.pucminas.aluguel.model.Rendimento;
import br.pucminas.aluguel.model.Usuario;
import br.pucminas.aluguel.repository.ClienteRepository;
import br.pucminas.aluguel.repository.PedidoAluguelRepository;
import br.pucminas.aluguel.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ClienteService {
    private final ClienteRepository clientes;
    private final UsuarioRepository usuarios;
    private final PedidoAluguelRepository pedidos;
    private final PasswordEncoder encoder;

    public ClienteService(ClienteRepository clientes, UsuarioRepository usuarios,
                          PedidoAluguelRepository pedidos, PasswordEncoder encoder) {
        this.clientes = clientes;
        this.usuarios = usuarios;
        this.pedidos = pedidos;
        this.encoder = encoder;
    }

    @Transactional
    public Cliente criar(ClienteRequest req) {
        if (req.senha() == null || req.senha().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha é obrigatória");
        if (usuarios.existsByLogin(req.login()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Login já cadastrado");
        if (clientes.existsByCpf(req.cpf()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado");
        Cliente c = new Cliente();
        preencher(c, req);
        return clientes.save(c);
    }

    public List<Cliente> listar() {
        return clientes.findAll();
    }

    /** Agentes acessam qualquer cliente; um cliente acessa apenas o próprio cadastro. */
    public Cliente buscar(Long id, Usuario solicitante) {
        if (!(solicitante instanceof Agente) && !solicitante.getId().equals(id))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado");
        return clientes.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }

    @Transactional
    public Cliente atualizar(Long id, ClienteRequest req, Usuario solicitante) {
        Cliente c = buscar(id, solicitante);
        if (!c.getLogin().equals(req.login()) && usuarios.existsByLogin(req.login()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Login já cadastrado");
        if (!c.getCpf().equals(req.cpf()) && clientes.existsByCpf(req.cpf()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado");
        preencher(c, req);
        return clientes.save(c);
    }

    @Transactional
    public void excluir(Long id, Usuario solicitante) {
        Cliente c = buscar(id, solicitante);
        pedidos.deleteByCliente(c);
        clientes.delete(c);
    }

    private void preencher(Cliente c, ClienteRequest req) {
        c.setLogin(req.login());
        if (req.senha() != null && !req.senha().isBlank()) c.setSenha(encoder.encode(req.senha()));
        c.setNome(req.nome());
        c.setRg(req.rg());
        c.setCpf(req.cpf());
        c.setEndereco(req.endereco());
        c.setProfissao(req.profissao());
        c.getRendimentos().clear();
        if (req.rendimentos() != null)
            req.rendimentos().forEach(r -> c.getRendimentos().add(new Rendimento(r.entidadeEmpregadora(), r.valor())));
    }
}
