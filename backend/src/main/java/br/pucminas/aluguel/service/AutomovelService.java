package br.pucminas.aluguel.service;

import br.pucminas.aluguel.model.Automovel;
import br.pucminas.aluguel.repository.AutomovelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutomovelService {
    private final AutomovelRepository automoveis;

    public AutomovelService(AutomovelRepository automoveis) {
        this.automoveis = automoveis;
    }

    public List<Automovel> listar() {
        return automoveis.findAll();
    }

    public Automovel criar(Automovel automovel) {
        return automoveis.save(automovel);
    }
}
