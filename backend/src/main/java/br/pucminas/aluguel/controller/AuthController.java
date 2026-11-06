package br.pucminas.aluguel.controller;

import br.pucminas.aluguel.dto.UsuarioResponse;
import br.pucminas.aluguel.service.UsuarioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/** Login: o front-end envia usuário/senha (HTTP Basic); se válidos, retorna os dados do usuário. */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UsuarioService usuarios;

    public AuthController(UsuarioService usuarios) {
        this.usuarios = usuarios;
    }

    @GetMapping("/me")
    public UsuarioResponse me(Principal principal) {
        return UsuarioResponse.from(usuarios.atual(principal));
    }
}
