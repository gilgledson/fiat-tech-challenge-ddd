# TODO — Tech Challenge Fase 3

Checklist do que ainda falta para atender aos requisitos obrigatórios da Fase 3
(`13SOAT - Fase 3 - Tech Challenge.pdf`), com base no estado atual do repositório
(herdado da Fase 2: Quarkus + AKS + Terraform + Postgres gerenciado + HPA + CI/CD
de deploy já implementados).

> Legenda: o que já está pronto da Fase 2 **não** está listado aqui. Isto é só o
> que falta fazer para a Fase 3.

## Autenticação e API Gateway

- [x] Implementar um API Gateway (Traefik) na frente da aplicação — o
      `oficina-api-service` agora é `ClusterIP` e o tráfego externo entra pelo
      `Service traefik` (LoadBalancer) e é roteado via `Ingress` até a
      aplicação. Ver [`k8s/gateway/`](k8s/gateway) e a seção
      [API Gateway (Traefik)](README.md#-api-gateway-traefik) no README.
      Pendente: validar em cluster real (`terraform apply` + push na `main`)
      e, quando a autenticação por CPF (próximo item) existir, mover a
      validação para o gateway (Middleware `ForwardAuth`).
- [x] Proteger rotas sensíveis com autenticação via **CPF** — os endpoints
      protegidos por `@RolesAllowed({"CLIENTE", ...})` (ex:
      `GET /api/ordens/{id}`, `POST /api/ordens/{id}/aprovar`) já existiam e
      validam qualquer JWT com `groups=CLIENTE`, independente de quem emitiu.
      O login por CPF (item abaixo) é um **segundo modo de login**, só para
      clientes — o login por e-mail/senha da equipe interna
      (`UsuarioController`/`EfetuarLoginUseCaseImpl`) continua intacto.
      Pendente: nenhuma rota valida que o `CLIENTE` autenticado é o **dono**
      da OS que está acessando (gap de autorização pré-existente, não
      introduzido por essa mudança — só ficou mais visível agora).
- [x] Criar uma **Function Serverless** (Azure Functions, Node.js) em
      [`azure-function-auth-cpf/`](azure-function-auth-cpf) que:
  - [x] Valida o CPF do cliente (dígito verificador, formato).
  - [x] Consulta a existência e o status (`deletado_em`) do cliente direto no
        Postgres.
  - [x] Gera e devolve um JWT RS256 assinado com a mesma chave/issuer que a
        API já valida (`groups: ["CLIENTE"]`) — sem nenhuma mudança de config
        no lado Java.
  - Terraform em [`infra/function.tf`](infra/function.tf) (Function App Linux,
    plano Consumption).
  - **Validado localmente de ponta a ponta** (docker-compose + `func start` +
    Newman): criar cliente com CPF válido → login na Function → token aceito
    em rota `CLIENTE` da API principal (200) → rejeitado em rota staff-only
    (403) → sem token (401) → CPF inválido (400) → cliente inexistente (404).
    8 requests / 12 assertions, todas passando. Cobertura em
    [`docs/postman_collection.json`](docs/postman_collection.json), pasta
    "Fase 3 - Autenticação CPF (Function Serverless)".
  - **Pendente**: provisionar de verdade na nuvem (`terraform apply` com
    `TF_VAR_jwt_private_key_pem` = conteúdo de
    `src/main/resources/privateKey.pem`) e fazer o deploy do código
    (`func azure functionapp publish`) — validado só localmente até aqui.

## Estrutura de Repositórios e CI/CD

- [ ] Separar o projeto em **4 repositórios**:
  - [ ] Lambda (Function Serverless).
  - [ ] Infraestrutura Kubernetes (Terraform).
  - [ ] Infraestrutura do Banco de Dados Gerenciado (Terraform).
  - [ ] Aplicação principal (Kubernetes) — este repositório atual.
- [ ] Configurar CI/CD (GitHub Actions ou similar) em cada um dos 4 repositórios,
      com deploy automático para a nuvem.
- [ ] Proteger a branch `main`/`master` em cada repositório (sem commits diretos,
      PR obrigatório para merge).
- [ ] Garantir deploy automático para as branches de homologação e produção em
      todos os repositórios (hoje só existe deploy automático da aplicação
      principal via `ci.yml`).

## Monitoramento e Observabilidade

- [x] Integrar uma ferramenta de observabilidade — **New Relic** escolhido
      (free tier, nativo em OTel, setup mais simples que Datadog pro cluster
      pequeno atual).
- [x] Implementar logs estruturados em JSON com correlação entre requisições
      — extensão `quarkus-logging-json` +
      [`CorrelationIdFilter`](src/main/java/br/com/fiap/oficina/api/shared/infrastructure/web/CorrelationIdFilter.java)
      (gera/propaga `X-Correlation-Id`, popula o MDC, loga método+path+status+duração
      por requisição). **Validado localmente**: JSON válido, `correlationId`
      aparece no MDC, header devolvido na resposta. Limitação conhecida: não
      aparece em respostas 401 de autenticação (rejeitadas antes da cadeia de
      filtros JAX-RS completar) — baixo impacto, não persegui.
- [x] Monitorar latência das APIs — New Relic Java Agent
      ([Dockerfile.jvm](src/main/docker/Dockerfile.jvm), baixado na build,
      anexado via `-javaagent`) instrumenta JAX-RS/JDBC automaticamente.
      **Validado localmente**: agent carrega, detecta ausência de
      `NEW_RELIC_LICENSE_KEY` e se desliga sozinho sem derrubar a app — dado
      real de latência só aparece com uma conta/license key de verdade.
- [x] Monitorar consumo de recursos do Kubernetes (CPU, memória) — chart Helm
      oficial `newrelic/nri-bundle`, values em
      [k8s/observability/newrelic-values.yaml](k8s/observability/newrelic-values.yaml).
      Instalação é passo manual (como o `terraform apply`), não entrou no
      `ci.yml`. **Não testado contra cluster real** (Helm não usado em
      nenhum outro lugar do projeto; YAML validado só sintaticamente).
- [x] Monitorar healthchecks e uptime — endpoints `/q/health/{started,live,ready}`
      já existiam (SmallRye Health) mas **não tinham probes no K8s** (achado
      durante a sessão do Gateway); adicionei `startupProbe`/`readinessProbe`/
      `livenessProbe` em [k8s/app/deployment.yaml](k8s/app/deployment.yaml).
      **Validado localmente**: os 3 endpoints respondem 200.
- [x] Configurar alertas para falhas no processamento de ordens de serviço —
      `newrelic_alert_policy` + 2 `newrelic_nrql_alert_condition` (falhas em
      `/api/ordens`, latência alta) em [infra/newrelic.tf](infra/newrelic.tf).
      Canal de notificação por e-mail é opcional
      (`TF_VAR_alert_notification_email`). **Validado com
      `terraform validate`/`plan`** contra o provider real (schema correto),
      não aplicado numa conta de verdade.
- [x] Expor dashboards com volume diário de OS, tempo médio por status
      (aproximado via latência das transições de status — não são eventos de
      negócio customizados) e erros/falhas por endpoint —
      `newrelic_one_dashboard` em [infra/newrelic.tf](infra/newrelic.tf).
      Mesma ressalva de validação acima.
- **Pendente geral**: nada disso foi testado contra uma conta New Relic real
  (não tenho acesso a credenciais) nem contra o cluster AKS — validação foi
  toda local (docker-compose) + `terraform validate`/`plan` com credenciais
  fake. Antes de gravar o vídeo de demonstração, alguém com a conta New
  Relic precisa: (1) setar `NEW_RELIC_LICENSE_KEY` no GitHub Secrets e no
  cluster, (2) rodar `terraform apply` com `TF_VAR_newrelic_account_id` e
  `TF_VAR_newrelic_api_key`, (3) instalar o Helm chart no cluster.

## Documentação da Arquitetura

- [ ] Diagrama de Componentes (visão de nuvem, APIs, banco e monitoramento).
- [ ] Diagrama de Sequência para o fluxo de autenticação e abertura de ordens
      de serviço.
- [ ] RFCs para decisões técnicas relevantes (escolha da nuvem, do banco, da
      estratégia de autenticação).
- [ ] ADRs para decisões arquiteturais permanentes (padrão de comunicação, uso
      de HPA etc.).
- [ ] Justificativa formal da escolha do banco de dados e ajustes no modelo
      relacional, com diagrama ER (DER) e explicação dos relacionamentos.

## README.md de cada repositório

- [ ] Atualizar/criar README com: descrição do propósito, tecnologias, passos
      de execução/deploy, diagrama de arquitetura específico e link do
      Swagger/Postman — em cada um dos 4 repositórios (o README atual cobre
      bem a Fase 2, mas não os itens novos da Fase 3).

## Vídeo de demonstração

- [ ] Gravar novo vídeo (até 15 min, YouTube/Vimeo) demonstrando:
  - [ ] Autenticação com CPF.
  - [ ] Execução da pipeline CI/CD.
  - [ ] Deploy automatizado.
  - [ ] Consumo das APIs protegidas.
  - [ ] Dashboard de monitoramento com análise ao vivo.
  - [ ] Logs e traces em execução.

## Entrega no Portal do Aluno

- [ ] Montar PDF único com: links dos 4 repositórios, link do vídeo (≤15 min),
      links das documentações e confirmação de que o usuário
      `soat-architecture` foi adicionado a todos os repositórios.

## Achado à parte (segurança, fora do escopo da Fase 3)

- [ ] Revogar/rotacionar o Personal Access Token do GitHub exposto em texto
      puro na URL do remote `origin` (`.git/config`) e reconfigurar o remote
      via SSH ou `gh auth login`.

## Corrigido: regressão no Dockerfile (achada ao validar o teste de CPF)

- [x] `POST /api/orcamentos/{id}/aceitar` retornava 500 (e derrubava em
      cascata 8 outras assertions da collection Newman: aprovação automática
      da OS, baixa de estoque, valor final do orçamento, listagem de OS,
      relatório de esforço). Causa raiz: ao trocar a imagem base do
      [Dockerfile.jvm](src/main/docker/Dockerfile.jvm) de
      `registry.access.redhat.com/ubi8/openjdk-17` para `eclipse-temurin:17-jre`
      (pra resolver o 502 do registry da Red Hat no CI), perdi a convenção do
      OpenShift de deixar `/deployments` gravável pelo grupo `0` — o
      `LocalFileStorageService` (fallback `STORAGE_TYPE=local`) não conseguia
      mais criar `uploads/assinaturas/`. Corrigido com
      `RUN chmod -R g+rwX /deployments` no Dockerfile. Validado: collection
      completa (51 requests / 57 assertions) passando 100% localmente.
