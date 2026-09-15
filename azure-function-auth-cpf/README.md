# Function Serverless — Autenticação por CPF

Azure Function (Node.js, HTTP trigger, plano Consumption) que autentica
**clientes** da Oficina API por CPF, sem exigir e-mail/senha. É a implementação
do requisito da Fase 3: *"Validar o CPF do cliente; consultar a existência e o
status do cliente na base de dados; gerar e devolver um token JWT válido"*.

Login de equipe interna (`ADMIN`/`ATENDENTE`/`MECANICO`) continua funcionando
como já existia, via `POST /api/usuarios/login` na API principal — essa
Function não substitui aquele fluxo, só adiciona um segundo modo de login
específico para o cliente final.

## Como funciona

```
POST /api/auth/cpf   { "cpf": "529.982.247-25" }
        │
        ├── 400 se o CPF for inválido (formato/dígito verificador)
        ├── 404 se não existir cliente com esse CPF
        ├── 403 se o cliente existir mas estiver inativo (soft-deleted)
        └── 200 { "access_token": "...", "token_type": "Bearer", "expires_in": 28800 }
```

O token emitido usa **o mesmo par de chaves RSA e o mesmo issuer** que a API
Quarkus já usa para os logins por e-mail/senha
(`src/main/resources/privateKey.pem` / `publicKey.pem`,
`smallrye.jwt.verify.issuer=oficina-api-interna`), com `groups: ["CLIENTE"]`.
Isso significa que o `@RolesAllowed({"CLIENTE"})` já existente nos endpoints
da API (ex: `GET /api/ordens/{id}`, `POST /api/ordens/{id}/aprovar`) valida
esse token **sem nenhuma alteração de código do lado Java** — a validação lá é
inteiramente declarativa a partir do claim `groups`.

## Tecnologias

- Node.js 18+, [Azure Functions Programming Model v4](https://learn.microsoft.com/azure/azure-functions/functions-reference-node)
- [`jsonwebtoken`](https://www.npmjs.com/package/jsonwebtoken) — assina o JWT em RS256
- [`pg`](https://node-postgres.com/) — consulta direta ao Postgres (mesmo banco gerenciado da API)

## Variáveis de ambiente (App Settings)

| Variável | Descrição |
|---|---|
| `DB_HOST` | Host do Postgres Flexible Server (ex: `oficina-postgres-server.postgres.database.azure.com`) |
| `DB_PORT` | Porta (padrão `5432`) |
| `DB_NAME` | Nome do banco (padrão `postgres`) |
| `DB_USER` | Usuário do Postgres |
| `DB_PASSWORD` | Senha do Postgres — **nunca commitar**, só via App Setting/Key Vault |
| `DB_SSL` | `"true"` (padrão) para exigir SSL, `"false"` para desabilitar |
| `JWT_ISSUER` | Issuer do token (padrão `oficina-api-interna`, deve bater com `smallrye.jwt.verify.issuer` da API) |
| `JWT_EXPIRES_IN_SECONDS` | Validade do token em segundos (padrão `28800` = 8h, igual ao login por e-mail/senha) |
| `JWT_PRIVATE_KEY` | Conteúdo do **mesmo** `privateKey.pem` da API — **nunca commitar**, só via App Setting/Key Vault |

Copie `local.settings.json.example` para `local.settings.json` (já
gitignorado) e preencha os valores para rodar localmente.

## Rodando localmente

Requer [Azure Functions Core Tools v4](https://learn.microsoft.com/azure/azure-functions/functions-run-local).

```bash
npm install
cp local.settings.json.example local.settings.json
# preencher DB_PASSWORD e JWT_PRIVATE_KEY em local.settings.json
npm start   # roda `func start`, expõe em http://localhost:7071/api/auth/cpf
```

## Testes

```bash
npm test
```

Cobre a validação de CPF (formato, dígito verificador, sequências inválidas).
Não há teste de integração com banco/Azure nesta pasta — o teste é isolado da
infraestrutura de propósito, para rodar em qualquer ambiente sem
credenciais.

O teste de integração de ponta a ponta (cria cliente → login por CPF → usa o
token na API principal → casos negativos) vive na collection Postman em
[`../docs/postman_collection.json`](../docs/postman_collection.json), pasta
**"Fase 3 - Autenticação CPF (Function Serverless)"** — roda com a Function
de pé (`npm start`) e a API principal também rodando. Veja a seção de Newman
no [README principal](../README.md#-testes).

## Deploy

Provisionado via Terraform em [`../infra/function.tf`](../infra/function.tf)
(Function App Linux, plano Consumption Y1, Node 20). Deploy do código:

```bash
func azure functionapp publish <nome-da-function-app>
```

> Nota de organização: este código vive nesta pasta do monorepo por
> enquanto. Quando o projeto for separado nos 4 repositórios exigidos pela
> Fase 3, esta pasta vira o repositório próprio de "Lambda / Function
> Serverless".
