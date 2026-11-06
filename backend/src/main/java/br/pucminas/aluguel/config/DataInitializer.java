package br.pucminas.aluguel.config;

import br.pucminas.aluguel.model.Agente;
import br.pucminas.aluguel.model.Automovel;
import br.pucminas.aluguel.model.TipoAgente;
import br.pucminas.aluguel.repository.AgenteRepository;
import br.pucminas.aluguel.repository.AutomovelRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/** Cadastra agentes (empresa e banco) e automóveis de exemplo quando o banco está vazio. */
@Component
public class DataInitializer implements CommandLineRunner {
    private final AgenteRepository agentes;
    private final AutomovelRepository automoveis;
    private final PasswordEncoder encoder;

    public DataInitializer(AgenteRepository agentes, AutomovelRepository automoveis, PasswordEncoder encoder) {
        this.agentes = agentes;
        this.automoveis = automoveis;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (agentes.count() == 0) {
            agentes.save(agente("empresa", "empresa123", "Locadora Minas", "11.111.111/0001-11", TipoAgente.EMPRESA));
            agentes.save(agente("banco", "banco123", "Banco PUC", "22.222.222/0001-22", TipoAgente.BANCO));
        }
        if (automoveis.count() == 0) {
            automoveis.save(new Automovel("MAT-0001", 2023, "Fiat", "Argo", "ABC1D23"));
            automoveis.save(new Automovel("MAT-0002", 2024, "Volkswagen", "Polo", "DEF4G56"));
            automoveis.save(new Automovel("MAT-0003", 2022, "Chevrolet", "Onix", "HIJ7K89"));
        }
    }

    private Agente agente(String login, String senha, String nome, String cnpj, TipoAgente tipo) {
        Agente a = new Agente();
        a.setLogin(login);
        a.setSenha(encoder.encode(senha));
        a.setNome(nome);
        a.setCnpj(cnpj);
        a.setTipo(tipo);
        return a;
    }
}
