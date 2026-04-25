# 🔧 Oficina API

> API RESTful para gestão de uma **Oficina Mecânica**, desenvolvida como parte do **Tech Challenge da FIAP**, aplicando os princípios de **Domain-Driven Design (DDD)** e **Clean Architecture**.

![Coverage](.github/badges/jacoco.svg)

---

## 📋 Sumário

- [Sobre o Projeto](#-sobre-o-projeto)
- [Arquitetura](#-arquitetura)
- [Módulos](#-módulos)
- [Tecnologias](#-tecnologias)
- [Banco de Dados](#-banco-de-dados)
- [Como Executar](#-como-executar)
- [Endpoints](#-endpoints)
- [Testes](#-testes)
- [Cobertura de Código](#-cobertura-de-código)

---

## 🚀 Sobre o Projeto

A **Oficina API** é um sistema backend para gerenciamento completo de uma oficina mecânica, cobrindo desde o catálogo de produtos e serviços até o controle de clientes, veículos e ordens de serviço.

O projeto foi desenvolvido utilizando **Quarkus**, o framework Java nativo em nuvem, com foco em alta performance e arquitetura limpa.

---

## 🏛️ Arquitetura

O projeto segue os princípios de **Clean Architecture** combinados com **Domain-Driven Design (DDD)**, organizando o código em camadas bem definidas e com dependências apontando sempre para o domínio.

```
src/main/java/.../modules/<contexto>/<agregado>/
│
├── domain/                  ← Entidades, Value Objects e regras de negócio puras
│   ├── entity/
│   └── valueobject/
│
├── application/             ← Casos de uso (UseCases), repositórios (interfaces) e DTOs de saída
│   ├── dto/
│   ├── mapper/
│   ├── repository/          ← Interfaces (contratos)
│   └── usecase/
│
├── infrastructure/          ← Implementações concretas (JPA, Panache)
│   └── persistence/
│
└── api/                     ← Controllers REST, DTOs de entrada/saída e mappers de API
    ├── controller/
    └── dto/
```

### Contextos Delimitados (Bounded Contexts)

| Contexto | Agregados |
|---|---|
| `catalogo` | `produto`, `servico` |
| `atendimento` | `cliente`, `veiculo` |
| `identidade` | *(autenticação via JWT)* |

---

## 📦 Módulos

### 🗂️ Catálogo

| Recurso | Descrição |
|---|---|
| **Produto** | Gestão de peças e insumos com controle de estoque, código de barras e unidade de medida |
| **Serviço** | Gestão de serviços da oficina com tipos (`MANUTENCAO_PREVENTIVA`, `MANUTENCAO_CORRETIVA`) e produtos sugeridos |

### 🧾 Atendimento

| Recurso | Descrição |
|---|---|
| **Cliente** | Cadastro de clientes com endereço completo, CPF/CNPJ e vínculo com usuário |
| **Veículo** | Cadastro de veículos vinculados a clientes, com placa, marca, modelo e ano |

---

## ⚙️ Tecnologias

| Tecnologia | Versão | Uso |
|---|---|---|
| [Quarkus](https://quarkus.io/) | 3.34.5 | Framework principal (runtime Java nativo em nuvem) |
| [Java](https://www.java.com/) | 17 | Linguagem de programação |
| [Hibernate ORM + Panache](https://quarkus.io/guides/hibernate-orm-panache) | — | Persistência e repositórios JPA |
| [PostgreSQL](https://www.postgresql.org/) | — | Banco de dados relacional |
| [Flyway](https://flywaydb.org/) | — | Versionamento e migração de banco de dados |
| [SmallRye JWT](https://quarkus.io/guides/security-jwt) | — | Autenticação e autorização via tokens JWT |
| [SmallRye OpenAPI](https://quarkus.io/guides/openapi-swaggerui) | — | Documentação automática da API (Swagger UI) |
| [Hibernate Validator](https://hibernate.org/validator/) | — | Validação de dados de entrada (Bean Validation) |
| [Lombok](https://projectlombok.org/) | 1.18.30 | Redução de boilerplate (getters, construtores) |
| [JUnit 5](https://junit.org/junit5/) | — | Framework de testes unitários |
| [Mockito](https://site.mockito.org/) | 5.11.0 | Mock de dependências nos testes unitários |
| [JaCoCo](https://www.jacoco.org/) | 0.8.12 | Cobertura de código dos testes |
| [Maven](https://maven.apache.org/) | — | Gerenciamento de dependências e build |

---

## 🗄️ Banco de Dados

O schema é versionado via **Flyway** com migrations incrementais:

| Migration | Descrição |
|---|---|
| `V1.0.0` | Criação da tabela de usuários |
| `V1.0.1` | Criação das tabelas de catálogo (`PRODUTO`, `SERVICO`) |
| `V1.0.2` | Criação das tabelas de atendimento (`CLIENTE`, `VEICULO`) |
| `V1.0.3` | Criação da tabela de Ordem de Serviço |
| `V1.0.4` | Criação das tabelas de itens da OS |
| `V1.0.5` | Criação da tabela de fatura |
| `V1.0.6` | Alterações nas tabelas de `CLIENTE` e `VEICULO` (exclusão lógica, email) |
| `V2` *(testdata)* | Seeds de produtos e serviços para ambiente de testes |

---

## ▶️ Como Executar

### Pré-requisitos

- Java 17+
- Maven 3.8+
- Docker & Docker Compose

### 1. Build do Pacote

Gere o artefato da aplicação ignorando os testes unitários:

```bash
./mvnw clean package -DskipTests
```

### 2. Subir o ambiente com Docker Compose

Com o pacote gerado, inicie os containers do banco de dados e da API:

```bash
docker-compose up -d
```

> A aplicação estará disponível em: `http://localhost:8383`

### 3. Acesse o Swagger UI

A documentação completa da API pode ser acessada em:
[http://localhost:8383/q/swagger-ui/#/](http://localhost:8383/q/swagger-ui/#/)

---

## 🌐 Endpoints

Todos os endpoints seguem o padrão REST e retornam respostas em JSON.
A documentação interativa completa está disponível no Swagger UI após subir a aplicação.

### Catálogo

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/produtos` | Listar produtos (paginado) |
| `POST` | `/api/produtos` | Cadastrar produto |
| `PUT` | `/api/produtos/{id}` | Editar produto |
| `DELETE` | `/api/produtos/{id}` | Inativar / deletar produto |
| `POST` | `/api/produtos/{id}/ativacao` | Reativar produto |
| `GET` | `/api/servicos` | Listar serviços (paginado) |
| `POST` | `/api/servicos` | Cadastrar serviço |
| `PUT` | `/api/servicos/{id}` | Editar serviço |
| `DELETE` | `/api/servicos/{id}` | Inativar / deletar serviço |
| `POST` | `/api/servicos/{id}/ativacao` | Reativar serviço |

### Atendimento

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/clientes` | Listar clientes (paginado) |
| `POST` | `/api/clientes` | Cadastrar cliente |
| `PUT` | `/api/clientes/{id}` | Editar cliente |
| `DELETE` | `/api/clientes/{id}` | Inativar / deletar cliente |
| `POST` | `/api/clientes/{id}/ativacao` | Reativar cliente |
| `GET` | `/api/veiculos` | Listar veículos (paginado) |
| `POST` | `/api/veiculos` | Cadastrar veículo |
| `PUT` | `/api/veiculos/{id}` | Editar veículo |
| `DELETE` | `/api/veiculos/{id}` | Inativar / deletar veículo |
| `POST` | `/api/veiculos/{id}/ativacao` | Reativar veículo |

### Paginação

Todos os endpoints de listagem aceitam os seguintes query params:

| Parâmetro | Tipo | Padrão | Descrição |
|---|---|---|---|
| `pagina` | `int` | `0` | Número da página (começa em 0) |
| `tamanho` | `int` | `10` | Itens por página (máx. 100) |
| `incluir_inativos` | `boolean` | `false` | Inclui registros excluídos logicamente |

**Formato da resposta paginada:**
```json
{
  "itens": [...],
  "pagina_atual": 0,
  "tamanho_pagina": 10,
  "total_paginas": 3,
  "total_elementos": 25
}
```

---

## 🧪 Testes

O projeto conta com testes **unitários puros** (sem dependência do container Quarkus), utilizando **JUnit 5** e **Mockito**.

### Executar os testes

```bash
./mvnw test
```

### Suites de teste

| Suite | Cenários | Descrição |
|---|---|---|
| `ProdutoTest` | 10 | Validações do domínio da entidade Produto |
| `CadastrarProdutoUseCaseTest` | 2 | Cadastro de produto (sucesso e duplicidade) |
| `EditarProdutoUseCaseTest` | 2 | Edição de produto (sucesso e not found) |
| `AtivarInativarProdutoUseCaseTest` | 6 | Ciclo de vida do produto |
| `CadastrarServicoUseCaseTest` | 2 | Cadastro de serviço com e sem produtos sugeridos |
| `CadastrarClienteUseCaseTest` | 3 | Cadastro de cliente (sucesso, CPF duplicado, usuário duplicado) |
| `EditarClienteUseCaseTest` | 2 | Edição de cliente |
| `AtivarInativarClienteUseCaseTest` | 5 | Ciclo de vida do cliente |
| `VeiculoTest` | 9 | Validações do domínio da entidade Veículo |
| `CadastrarVeiculoUseCaseTest` | 2 | Cadastro de veículo (sucesso e placa duplicada) |
| **Total** | **43** | |

---

## 📊 Cobertura de Código

A cobertura é gerada automaticamente com **JaCoCo** ao executar os testes.

```bash
./mvnw clean test
```

O relatório HTML é gerado em:

```
target/site/jacoco/index.html
```

> As camadas de infraestrutura (JPA Entities, Repositories) e controllers são excluídas da cobertura, pois não contêm lógica de negócio testável unitariamente.

---

## 👥 Equipe

Desenvolvido como parte do **Tech Challenge — FIAP**.

---

## 📄 Licença

Este projeto é de uso acadêmico e foi desenvolvido para fins de aprendizagem no contexto da **FIAP**.
