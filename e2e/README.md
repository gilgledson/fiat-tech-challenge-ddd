# Teste integrado dos três microsserviços

Sobe o OS Service, o Billing Service e o Execution Service com seus bancos, o
RabbitMQ e um gateway local, e roda a collection do Postman com o Newman.

## Pré-requisitos

- Docker com Docker Compose.
- JDK 17.
- Os três repositórios lado a lado na mesma pasta:

```
fiat-tech-challenge-ddd/      (este repositório — OS Service)
oficina-billing-service/
oficina-execution-service/
```

## 1. Empacotar os serviços

Em cada um dos três repositórios:

```bash
./mvnw clean package -DskipTests
```

## 2. Subir o ambiente

A partir desta pasta (`e2e/`):

```bash
docker compose up -d --build
```

| O quê | Onde |
|---|---|
| API (gateway único para os três serviços) | `http://localhost:8080` |
| Painel do RabbitMQ | `http://localhost:15672` (`oficina` / `oficina`) |

O gateway ([gateway.conf](gateway.conf)) roteia como o Traefik em produção:
`/api/orcamentos` vai para o Billing; `/api/execucoes`, `/api/relatorios` e
`/api/funcionarios` vão para o Execution; o resto vai para o OS Service.

## 3. Rodar a collection

Com o Newman instalado (`npm install -g newman`), a partir da raiz do repositório:

```bash
newman run docs/postman_collection.json --env-var "baseUrl=http://localhost:8080"
```

Sem instalar o Newman, pela imagem Docker oficial (a partir da raiz do repositório):

```bash
docker run --rm --network oficina-e2e_default -v "$PWD/docs:/etc/newman" postman/newman run postman_collection.json --env-var "baseUrl=http://gateway"
```

A pasta "Fase 3 - Autenticação CPF" depende da Azure Function rodando em
`localhost:7071`. Para rodar só o que este ambiente cobre, informe as pastas:

```bash
newman run docs/postman_collection.json --env-var "baseUrl=http://localhost:8080" --folder "Identidade" --folder "Atendimento" --folder "Catálogo" --folder "Operacional e Execução" --folder "Orçamento" --folder "Fase 2 - Novos Endpoints" --folder "Relatórios" --folder "Fase 4 - Saga (compensação)"
```

## Como a collection lida com a Saga

Os passos da OS agora atravessam serviços por eventos, então o efeito de um
comando leva alguns instantes para aparecer no serviço vizinho. As requisições
que dependem disso se repetem, uma vez por segundo e por até 30 segundos, até
o estado esperado aparecer; só então os testes rodam. Se o estado não chegar,
o teste falha normalmente.

A pasta "Fase 4 - Saga (compensação)" cobre um caminho de falha: a oficina
reporta que não consegue concluir a OS, e a collection confere que a OS foi
cancelada, o estoque reservado foi liberado e o orçamento foi cancelado.

## 4. Derrubar o ambiente

```bash
docker compose down -v
```

O `-v` apaga os volumes, então a próxima subida começa com os bancos vazios.
