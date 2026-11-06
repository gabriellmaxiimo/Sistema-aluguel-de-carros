package br.pucminas.aluguel;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FluxoAluguelTest {

    @Value("${local.server.port}")
    int port;

    RestClient client(String login, String senha) {
        return RestClient.builder().baseUrl("http://localhost:" + port)
                .defaultHeaders(h -> { if (login != null) h.setBasicAuth(login, senha); }).build();
    }

    Map<String, Object> clienteBody(String login, String cpf, int rendimentos) {
        var rends = java.util.stream.IntStream.range(0, rendimentos)
                .mapToObj(i -> Map.of("entidadeEmpregadora", "Empresa " + i, "valor", 3000)).toList();
        return Map.of("login", login, "senha", "123", "nome", "Maria", "rg", "MG-1", "cpf", cpf,
                "endereco", "Rua A", "profissao", "Dev", "rendimentos", rends);
    }

    @Test
    void fluxoCompleto() {
        // sem login: acesso negado
        assertThrows(HttpClientErrorException.Unauthorized.class,
                () -> client(null, null).get().uri("/api/pedidos").retrieve().body(List.class));

        // cadastro de cliente: máximo 3 rendimentos
        assertThrows(HttpClientErrorException.BadRequest.class, () -> client(null, null).post().uri("/api/clientes")
                .body(clienteBody("x", "000", 4)).retrieve().body(Map.class));
        Map<?, ?> cli = client(null, null).post().uri("/api/clientes")
                .body(clienteBody("maria", "111", 3)).retrieve().body(Map.class);

        RestClient maria = client("maria", "123");
        RestClient banco = client("banco", "banco123");
        RestClient empresa = client("empresa", "empresa123");

        assertEquals("CLIENTE", maria.get().uri("/api/auth/me").retrieve().body(Map.class).get("perfil"));
        assertThrows(HttpClientErrorException.Unauthorized.class,
                () -> client("maria", "errada").get().uri("/api/auth/me").retrieve().body(Map.class));

        // cliente cria pedido -> PENDENTE
        Map<String, Object> pedido = Map.of("automovelId", 1, "dataInicio", "2026-12-01", "dataFim", "2026-12-10");
        Map<?, ?> p = maria.post().uri("/api/pedidos").body(pedido).retrieve().body(Map.class);
        assertEquals("PENDENTE", p.get("status"));
        Object id = p.get("id");

        // cliente não avalia; agente avalia
        assertThrows(HttpClientErrorException.Forbidden.class, () -> maria.post().uri("/api/pedidos/{id}/avaliacao", id)
                .body(Map.of("aprovado", true)).retrieve().body(Map.class));
        p = empresa.post().uri("/api/pedidos/{id}/avaliacao", id)
                .body(Map.of("aprovado", true, "parecer", "ok")).retrieve().body(Map.class);
        assertEquals("APROVADO", p.get("status"));

        // empresa não concede crédito; banco sim
        Map<String, Object> contrato = Map.of("tipoPropriedade", "BANCO",
                "credito", Map.of("valor", 5000, "taxaJuros", 1.5, "numeroParcelas", 12));
        assertThrows(HttpClientErrorException.Forbidden.class, () -> empresa.post().uri("/api/pedidos/{id}/contrato", id)
                .body(contrato).retrieve().body(Map.class));
        p = banco.post().uri("/api/pedidos/{id}/contrato", id).body(contrato).retrieve().body(Map.class);
        assertEquals("CONTRATADO", p.get("status"));
        assertEquals("Banco PUC", ((Map<?, ?>) p.get("contrato")).get("banco"));

        // contratado não pode ser cancelado
        assertThrows(HttpClientErrorException.Conflict.class,
                () -> maria.post().uri("/api/pedidos/{id}/cancelar", id).retrieve().body(Map.class));

        // novo pedido: cliente modifica e cancela
        Object id2 = maria.post().uri("/api/pedidos").body(pedido).retrieve().body(Map.class).get("id");
        p = maria.put().uri("/api/pedidos/{id}", id2)
                .body(Map.of("automovelId", 2, "dataInicio", "2026-12-05", "dataFim", "2026-12-06"))
                .retrieve().body(Map.class);
        assertEquals("2026-12-05", p.get("dataInicio"));
        assertEquals("CANCELADO", maria.post().uri("/api/pedidos/{id}/cancelar", id2).retrieve().body(Map.class).get("status"));

        // outro cliente não vê pedido da Maria
        client(null, null).post().uri("/api/clientes").body(clienteBody("joao", "222", 0)).retrieve().body(Map.class);
        assertThrows(HttpClientErrorException.Forbidden.class,
                () -> client("joao", "123").get().uri("/api/pedidos/{id}", id).retrieve().body(Map.class));

        // CRUD cliente: atualiza (senha em branco mantém) e exclui
        Map<String, Object> upd = new java.util.HashMap<>(clienteBody("maria", "111", 1));
        upd.put("senha", "");
        upd.put("nome", "Maria Silva");
        assertEquals("Maria Silva", maria.put().uri("/api/clientes/{id}", cli.get("id")).body(upd)
                .retrieve().body(Map.class).get("nome"));
        assertEquals(2, banco.get().uri("/api/clientes").retrieve().body(List.class).size());
        maria.delete().uri("/api/clientes/{id}", cli.get("id")).retrieve().toBodilessEntity();
        assertEquals(1, banco.get().uri("/api/clientes").retrieve().body(List.class).size());
    }
}
