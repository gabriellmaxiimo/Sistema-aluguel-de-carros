package br.pucminas.aluguel.repository;

import br.pucminas.aluguel.model.Automovel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutomovelRepository extends JpaRepository<Automovel, Long> {
}
