package br.pucminas.aluguel.service;

import br.pucminas.aluguel.model.Usuario;
import br.pucminas.aluguel.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

/** Autenticação: carrega usuários (clientes e agentes) cadastrados para o Spring Security. */
@Service
public class UsuarioService implements UserDetailsService {
    private final UsuarioRepository usuarios;

    public UsuarioService(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public UserDetails loadUserByUsername(String login) {
        Usuario u = usuarios.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        return User.withUsername(u.getLogin()).password(u.getSenha()).roles(u.getPerfil().name()).build();
    }

    public Usuario atual(Principal principal) {
        return usuarios.findByLogin(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }
}
