# Teste integrado dos três microsserviços

Sobe o OS Service, o Billing Service e o Execution Service com seus bancos, o
RabbitMQ e um gateway local, e roda a collection do Postman com o Newman e os
cenários BDD com o Cucumber.

## Pré-requisitos

- Docker com Docker Compose.
- JDK 17. Se o `java` não estiver no `PATH`, aponte o `JAVA_HOME` para ele
  (no macOS com Homebrew: `export JAVA_HOME=/opt/homebrew/opt/openjdk@17`).
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

O ambiente inclui um **Mercado Pago simulado** (WireMock, em
[mercadopago-fake](mercadopago-fake)), para o Billing criar a cobrança e
consultar pagamentos sem credenciais reais. O ID do pagamento notificado
decide a resposta: `aprovado-<ID do orçamento>` ou `recusado-<ID do orçamento>`.
O token e a chave do webhook no [docker-compose.yaml](docker-compose.yaml) são
fictícios.

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

## 4. Rodar os cenários BDD (Cucumber)

Os cenários em Gherkin ficam em
[bdd/src/test/resources/features](bdd/src/test/resources/features) e cobrem o
fluxo completo da OS com pagamento pelo Mercado Pago, a falha na execução com
compensação, a rejeição do orçamento, o pagamento recusado, a notificação com
assinatura inválida e o pagamento no balcão. A partir da raiz do repositório, com o ambiente no ar:

```bash
./mvnw -f e2e/bdd/pom.xml test
```

Para apontar para outro endereço, acrescente `-DbaseUrl=http://host:porta`. O
relatório em HTML sai em `e2e/bdd/target/cucumber/relatorio.html`.

Cada cenário cadastra o próprio cliente, veículo, peça e serviço, então eles
não dependem da collection nem um do outro. Como na collection, os passos que
dependem de um evento esperam até 30 segundos pelo estado esperado.

## 5. Derrubar o ambiente

```bash
docker compose down -v
```

O `-v` apaga os volumes, então a próxima subida começa com os bancos vazios.
