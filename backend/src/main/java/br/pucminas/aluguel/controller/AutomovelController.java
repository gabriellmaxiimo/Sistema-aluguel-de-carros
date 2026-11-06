package br.pucminas.aluguel.controller;

import br.pucminas.aluguel.model.Automovel;
import br.pucminas.aluguel.service.AutomovelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/automoveis")
public class AutomovelController {
    private final AutomovelService automoveis;

    public AutomovelController(AutomovelService automoveis) {
        this.automoveis = automoveis;
    }

    @GetMapping
    public List<Automovel> listar() {
        return automoveis.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Automovel criar(@Valid @RequestBody Automovel automovel) {
        return automoveis.criar(automovel);
    }
}
