package br.pucminas.aluguel.repository;

import br.pucminas.aluguel.model.Cliente;
import br.pucminas.aluguel.model.PedidoAluguel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoAluguelRepository extends JpaRepository<PedidoAluguel, Long> {
    List<PedidoAluguel> findByClienteOrderByIdDesc(Cliente cliente);
    List<PedidoAluguel> findAllByOrderByIdDesc();
    void deleteByCliente(Cliente cliente);
}
