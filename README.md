# 🏷 Sistema de Aluguel de Carros 👨‍💻

> [!NOTE]
> Sistema web para apoio à gestão de aluguéis de automóveis: **clientes** introduzem, modificam, consultam e cancelam pedidos pela Internet; **agentes** (empresas e bancos) modificam, avaliam financeiramente e executam os contratos — com crédito bancário opcional. Laboratório 02 da disciplina DIAW (Engenharia de Software — PUC Minas).

---

## 🚧 Status do Projeto

![Versão](https://img.shields.io/badge/Versão-v1.0.0-blue?style=for-the-badge)
![Java](https://img.shields.io/badge/Java-25-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.9-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9.11-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![React](https://img.shields.io/badge/React-19-61DAFB?style=for-the-badge&logo=react&logoColor=black)
![Vite](https://img.shields.io/badge/Vite-7-646CFF?style=for-the-badge&logo=vite&logoColor=white)
![Supabase](https://img.shields.io/badge/Supabase-PostgreSQL-3ECF8E?style=for-the-badge&logo=supabase&logoColor=white)

---

## 📚 Índice
- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades Principais](#-funcionalidades-principais)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Arquitetura](#-arquitetura)
- [Modelagem (UML)](#-modelagem-uml)
- [Instalação e Execução](#-instalação-e-execução)
  - [Pré-requisitos](#pré-requisitos)
  - [Variáveis de Ambiente](#-variáveis-de-ambiente)
  - [Instalação de Dependências](#-instalação-de-dependências)
  - [Inicialização do Banco de Dados (Supabase)](#-inicialização-do-banco-de-dados-supabase)
  - [Como Executar a Aplicação](#-como-executar-a-aplicação)
- [Deploy](#-deploy)
- [Estrutura de Pastas](#-estrutura-de-pastas)
- [API (Endpoints)](#-api-endpoints)
- [Testes](#-testes)
- [Documentações utilizadas](#-documentações-utilizadas)
- [Autores](#-autores)
- [Contribuição](#-contribuição)
- [Agradecimentos](#-agradecimentos)
- [Licença](#-licença)

---

## 📝 Sobre o Projeto

O sistema só pode ser utilizado após **cadastro prévio e autenticação** (usuário e senha). Após introduzido, cada pedido de aluguel é **analisado do ponto de vista financeiro** por um agente; em caso de parecer positivo, ele fica disponível para **execução do contrato**. Dependendo do tipo de contrato, o automóvel é registrado como propriedade do **cliente, da empresa ou do banco**, e o aluguel pode estar associado a um **contrato de crédito** concedido por um banco agente.

O sistema é dividido em dois subsistemas, conforme o enunciado:
1. **Gestão de pedidos e contratos** — servidor central (Spring Boot, MVC) exposto via API REST.
2. **Construção dinâmica das páginas web** — SPA em React + Vite executada no computador do cliente/agente, ligada ao servidor pela Internet.

Fluxo do pedido:

```
PENDENTE ──(agente: parecer positivo)──▶ APROVADO ──(agente: executa contrato)──▶ CONTRATADO
    │                                       │
    ├──(agente: parecer negativo)──▶ REPROVADO
    └──(cliente cancela)──▶ CANCELADO ◀─────┘
```

---

## ✨ Funcionalidades Principais

- 🔐 **Autenticação obrigatória:** login com usuário e senha (HTTP Basic + BCrypt) para clientes e agentes; apenas o cadastro de cliente é público.
- 👤 **CRUD de cliente:** RG, CPF, nome, endereço, profissão e até **3 rendimentos** (entidade empregadora + valor).
- 📝 **Pedidos (cliente):** introduzir, modificar (enquanto pendente), consultar status e cancelar.
- 🏦 **Pedidos (agente):** modificar, avaliar financeiramente (parecer positivo/negativo) e executar contrato.
- 📄 **Contrato:** registro da propriedade do automóvel (cliente/empresa/banco) e **contrato de crédito** opcional, permitido apenas a bancos.
- 🚗 **Automóveis:** matrícula, ano, marca, modelo e placa (cadastro feito por agentes).

---

## 🛠 Tecnologias Utilizadas

### 💻 Front-end
- React 19 + Vite 7 (JavaScript, `fetch` nativo)

### 🖥️ Back-end
- Java 25 (JDK 25)
- Spring Boot 4.0.x (Web MVC, Data JPA, Security, Validation)
- Apache Maven (com Maven Wrapper `mvnw`)
- Banco de dados: Supabase (PostgreSQL)
- Testes: JUnit 5 + H2 em memória

---

## 🏗 Arquitetura

Arquitetura **MVC** em camadas no back-end, com a *View* sendo construída dinamicamente pelo front-end React:

| Camada | Pacote | Responsabilidade |
|--------|--------|------------------|
| Controller | `controller` | Endpoints REST, recebe requisições e devolve DTOs |
| Service | `service` | Regras de negócio (fluxo de status, permissões, crédito só por banco) |
| Repository | `repository` | Acesso a dados (Spring Data JPA) |
| Model | `model` | Entidades JPA do domínio |
| DTO | `dto` | Objetos de entrada/saída da API (sem expor senha) |
| Config | `config` | Segurança (Spring Security, CORS) e carga inicial de dados |
| View | `frontend/` | Páginas React (construção dinâmica das páginas web) |

---

## 📐 Modelagem (UML)

Os modelos ficam em [`docs/`](docs/) e são versionados junto com o código (cada revisão incrementa a versão no título do diagrama):

| Artefato | Arquivo |
|----------|---------|
| Diagrama de Casos de Uso | [docs/diagramas/casos-de-uso.puml](docs/diagramas/casos-de-uso.puml) |
| Histórias de Usuário | [docs/historias-de-usuario.md](docs/historias-de-usuario.md) |
| Diagrama de Classes | [docs/diagramas/classes.puml](docs/diagramas/classes.puml) |
| Diagrama de Pacotes (Visão Lógica) | [docs/diagramas/pacotes.puml](docs/diagramas/pacotes.puml) |

> [!TIP]
> Os diagramas estão em [PlantUML](https://plantuml.com/). Para visualizar/exportar PNG, use a extensão *PlantUML* do VS Code (Alt+D) ou cole o conteúdo em https://www.plantuml.com/plantuml.

---

## 🔧 Instalação e Execução

### Pré-requisitos
- **Java JDK 25**
- **Node.js** LTS (v20 ou superior) e **npm**
- Um projeto no **Supabase** (gratuito) — https://supabase.com
- Maven **não** precisa estar instalado (o projeto usa o Maven Wrapper `mvnw`)

### 🔑 Variáveis de Ambiente

#### 1. Back-end (`backend/.env`, copiar de `backend/.env.example`)

| Variável | Descrição | Exemplo |
|----------|-----------|---------|
| `SUPABASE_DB_URL` | URL JDBC do banco Supabase | `jdbc:postgresql://aws-0-sa-east-1.pooler.supabase.com:5432/postgres?sslmode=require` |
| `SUPABASE_DB_USER` | Usuário do banco | `postgres.SEU_PROJECT_REF` |
| `SUPABASE_DB_PASSWORD` | Senha do banco | `sua-senha` |
| `SERVER_PORT` | Porta da API | `8080` |
| `CORS_ORIGINS` | Origem(ns) do front-end, separadas por vírgula | `http://localhost:5173` |

#### 2. Front-end (`frontend/.env`, copiar de `frontend/.env.example`)

| Variável | Descrição | Exemplo |
|----------|-----------|---------|
| `VITE_API_URL` | URL base da API | `http://localhost:8080` |

### 📦 Instalação de Dependências

```bash
git clone <URL_DO_SEU_REPOSITÓRIO>
cd <pasta-do-projeto>

# Front-end
cd frontend
npm install

# Back-end
cd ../backend
./mvnw clean install        # Windows: mvnw.cmd clean install
```

### 💾 Inicialização do Banco de Dados (Supabase)

1. Crie um projeto em https://supabase.com.
2. Em **Project Settings → Database → Connection string**, copie host, usuário e senha (use o *Session pooler* se sua rede não tiver IPv6).
3. Preencha `backend/.env`.
4. As tabelas são criadas automaticamente pelo Hibernate (`ddl-auto=update`) na primeira execução, e o `DataInitializer` cadastra dados de exemplo:

| Usuário | Senha | Perfil |
|---------|-------|--------|
| `empresa` | `empresa123` | Agente — Empresa |
| `banco` | `banco123` | Agente — Banco |

Além de 3 automóveis. Clientes se cadastram pela tela de login.

### ⚡ Como Executar a Aplicação

**Terminal 1 — Back-end**
```bash
cd backend
./mvnw spring-boot:run      # Windows: mvnw.cmd spring-boot:run
```
API em `http://localhost:8080`.

**Terminal 2 — Front-end**
```bash
cd frontend
npm run dev
```
Aplicação em `http://localhost:5173`.

---

## 🚀 Deploy

```bash
# Front-end (gera frontend/dist)
cd frontend && npm run build

# Back-end (gera backend/target/aluguel-carros-1.0.0.jar)
cd backend && ./mvnw clean package
java -jar backend/target/aluguel-carros-1.0.0.jar
```

Em produção, defina as variáveis de ambiente do back-end (incluindo `CORS_ORIGINS` com a URL do front) e `VITE_API_URL` no build do front-end.

---

## 📂 Estrutura de Pastas

```
.
├── README.md
├── docs/
│   ├── historias-de-usuario.md
│   └── diagramas/                # casos-de-uso, classes, pacotes (PlantUML)
├── backend/                      # Subsistema de gestão de pedidos e contratos
│   ├── pom.xml, mvnw, .env.example
│   └── src/
│       ├── main/java/br/pucminas/aluguel/
│       │   ├── controller/       # AuthController, ClienteController, AutomovelController, PedidoController
│       │   ├── service/          # UsuarioService, ClienteService, AutomovelService, PedidoService
│       │   ├── repository/       # Spring Data JPA
│       │   ├── model/            # Usuario, Cliente, Rendimento, Agente, Automovel, PedidoAluguel, Contrato, ContratoCredito
│       │   ├── dto/              # Requests/Responses da API
│       │   └── config/           # SecurityConfig, DataInitializer
│       ├── main/resources/application.properties
│       └── test/                 # Teste de integração do fluxo completo (H2)
└── frontend/                     # Subsistema de construção dinâmica das páginas web
    ├── index.html, vite.config.js, .env.example
    └── src/
        ├── App.jsx               # Navegação por perfil (cliente/agente)
        ├── services/api.js       # Chamadas à API + sessão de login
        └── pages/                # Login, ClienteForm, Pedidos, Clientes, Automoveis
```

---

## 🔌 API (Endpoints)

Todas as rotas exigem login (HTTP Basic), exceto `POST /api/clientes`.

| Método | Rota | Perfil | Descrição |
|--------|------|--------|-----------|
| GET | `/api/auth/me` | todos | Valida login e retorna o usuário |
| POST | `/api/clientes` | público | Cadastro de cliente |
| GET | `/api/clientes` | agente | Lista clientes |
| GET/PUT/DELETE | `/api/clientes/{id}` | agente ou o próprio cliente | Consulta/altera/exclui |
| GET | `/api/automoveis` | todos | Lista automóveis |
| POST | `/api/automoveis` | agente | Cadastra automóvel |
| POST | `/api/pedidos` | cliente | Introduz pedido |
| GET | `/api/pedidos` | todos | Cliente: os seus; agente: todos |
| GET/PUT | `/api/pedidos/{id}` | dono ou agente | Consulta/modifica |
| POST | `/api/pedidos/{id}/cancelar` | cliente | Cancela |
| POST | `/api/pedidos/{id}/avaliacao` | agente | `{ "aprovado": true, "parecer": "..." }` |
| POST | `/api/pedidos/{id}/contrato` | agente | `{ "tipoPropriedade": "CLIENTE", "credito": { "valor": 5000, "taxaJuros": 1.5, "numeroParcelas": 12 } }` (crédito só banco) |

Exemplo:
```bash
curl -u banco:banco123 http://localhost:8080/api/pedidos
```

---

## 🧪 Testes

```bash
cd backend
./mvnw test
```

O teste [`FluxoAluguelTest`](backend/src/test/java/br/pucminas/aluguel/FluxoAluguelTest.java) sobe a aplicação com H2 em memória e percorre via HTTP: acesso sem login, limite de 3 rendimentos, login inválido, criação → avaliação → contrato com crédito, bloqueio de crédito por empresa, modificação/cancelamento, isolamento entre clientes e CRUD de cliente.

---

## 🔗 Documentações utilizadas

- [Spring Boot](https://docs.spring.io/spring-boot/index.html)
- [Spring Security](https://docs.spring.io/spring-security/reference/index.html)
- [Supabase – Documentação](https://supabase.com/docs)
- [React](https://react.dev/reference/react)
- [Vite](https://vitejs.dev/config/)
- [PlantUML](https://plantuml.com/)

---

## 👥 Autores

| 👤 Nome | :octocat: GitHub | 📧 Gmail |
|---------|------------------|----------|
| _Seu nome_ | [_seu-usuario_](https://github.com/) | _seu-email@gmail.com_ |

---

## 🤝 Contribuição

1. Faça um fork do projeto.
2. Crie uma branch: `git checkout -b feature/minha-feature`.
3. Commit seguindo [Conventional Commits](https://www.conventionalcommits.org/pt-br/v1.0.0/): `git commit -m "feat: adiciona nova funcionalidade X"`.
4. Push: `git push origin feature/minha-feature`.
5. Abra um Pull Request.

---

## 🙏 Agradecimentos

- [Engenharia de Software PUC Minas](https://www.instagram.com/engsoftwarepucminas/)
- [Prof. Dr. João Paulo Aramuni](https://github.com/joaopauloaramuni)

---

## 📄 Licença

Este projeto é distribuído sob a **Licença MIT**.
