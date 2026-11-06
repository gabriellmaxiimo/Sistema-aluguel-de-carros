package br.pucminas.aluguel.repository;

import br.pucminas.aluguel.model.Agente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgenteRepository extends JpaRepository<Agente, Long> {
}
