# Observabilidade (Fase 3): alertas e dashboard da Oficina API como código.
# O APM em si (dados de latência/erro/transações) vem do New Relic Java Agent
# embutido na imagem (src/main/docker/Dockerfile.jvm) — aqui só provisionamos
# o que consome esses dados: alert policy, condições NRQL e o dashboard.

resource "newrelic_alert_policy" "oficina_alerts" {
  name                = "Oficina API - Alertas"
  incident_preference = "PER_CONDITION"
}

# "Configurar alertas para falhas no processamento de ordens de serviço"
resource "newrelic_nrql_alert_condition" "falhas_processamento_os" {
  policy_id                    = newrelic_alert_policy.oficina_alerts.id
  type                         = "static"
  name                         = "Falhas no processamento de Ordens de Serviço"
  enabled                      = true
  violation_time_limit_seconds = 3600
  aggregation_window           = 60
  aggregation_method           = "event_flow"
  aggregation_delay            = 120

  nrql {
    query = "SELECT count(*) FROM TransactionError WHERE appName = '${var.newrelic_app_name}' AND request.uri LIKE '%/api/ordens%'"
  }

  critical {
    operator              = "above"
    threshold             = 3
    threshold_duration    = 300
    threshold_occurrences = "at_least_once"
  }

  warning {
    operator              = "above"
    threshold             = 1
    threshold_duration    = 300
    threshold_occurrences = "at_least_once"
  }
}

# Latência alta nas APIs em geral (não só ordens de serviço)
resource "newrelic_nrql_alert_condition" "latencia_alta" {
  policy_id                    = newrelic_alert_policy.oficina_alerts.id
  type                         = "static"
  name                         = "Latência média das APIs acima do esperado"
  enabled                      = true
  violation_time_limit_seconds = 3600
  aggregation_window           = 60
  aggregation_method           = "event_flow"
  aggregation_delay            = 120

  nrql {
    query = "SELECT average(duration) FROM Transaction WHERE appName = '${var.newrelic_app_name}'"
  }

  critical {
    operator              = "above"
    threshold             = 2
    threshold_duration    = 300
    threshold_occurrences = "all"
  }

  warning {
    operator              = "above"
    threshold             = 1
    threshold_duration    = 300
    threshold_occurrences = "all"
  }
}

# Canal de notificação (opcional — só é criado se alert_notification_email
# for definido). Sem isso, as condições acima só mudam de estado dentro do
# New Relic, sem avisar ninguém.
resource "newrelic_notification_destination" "email" {
  count = var.alert_notification_email != "" ? 1 : 0
  name  = "oficina-api-email"
  type  = "EMAIL"

  property {
    key   = "email"
    value = var.alert_notification_email
  }
}

resource "newrelic_notification_channel" "email" {
  count          = var.alert_notification_email != "" ? 1 : 0
  name           = "oficina-api-email-channel"
  type           = "EMAIL"
  destination_id = newrelic_notification_destination.email[0].id
  product        = "IINT"

  property {
    key   = "subject"
    value = "Alerta Oficina API: {{issueTitle}}"
  }
}

resource "newrelic_workflow" "alertas_oficina" {
  count                 = var.alert_notification_email != "" ? 1 : 0
  name                  = "oficina-api-workflow"
  muting_rules_handling = "NOTIFY_ALL_ISSUES"

  issues_filter {
    name = "oficina-api-issues"
    type = "FILTER"

    predicate {
      attribute = "labels.policyIds"
      operator  = "EXACTLY_MATCHES"
      values    = [newrelic_alert_policy.oficina_alerts.id]
    }
  }

  destination {
    channel_id = newrelic_notification_channel.email[0].id
  }
}

# "Expor dashboards com: volume diário de OS, tempo médio de execução por
# status, erros e falhas nas integrações" — construído a partir dos dados de
# transação que o Java Agent já captura automaticamente (sem precisar
# instrumentar eventos de negócio customizados no código).
resource "newrelic_one_dashboard" "oficina" {
  name = "Oficina API - Visão Geral"

  page {
    name = "Operação"

    widget_billboard {
      title  = "Ordens de Serviço Abertas (últimas 24h)"
      row    = 1
      column = 1
      width  = 4
      height = 3

      nrql_query {
        account_id = var.newrelic_account_id
        query      = "SELECT count(*) FROM Transaction WHERE appName = '${var.newrelic_app_name}' AND request.uri = '/api/ordens' AND request.method = 'POST' SINCE 1 day ago"
      }
    }

    widget_line {
      title  = "Volume Diário de Ordens de Serviço Abertas"
      row    = 1
      column = 5
      width  = 8
      height = 3

      nrql_query {
        account_id = var.newrelic_account_id
        query      = "SELECT count(*) FROM Transaction WHERE appName = '${var.newrelic_app_name}' AND request.uri = '/api/ordens' AND request.method = 'POST' TIMESERIES 1 day SINCE 30 days ago"
      }
    }

    widget_line {
      title  = "Tempo Médio por Transição de Status da OS"
      row    = 4
      column = 1
      width  = 6
      height = 3

      nrql_query {
        account_id = var.newrelic_account_id
        query      = "SELECT average(duration) FROM Transaction WHERE appName = '${var.newrelic_app_name}' AND (request.uri LIKE '%/iniciar-diagnostico' OR request.uri LIKE '%/concluir-diagnostico' OR request.uri LIKE '%/iniciar-execucao' OR request.uri LIKE '%/concluir-execucao') FACET request.uri TIMESERIES 1 hour SINCE 7 days ago"
      }
    }

    widget_line {
      title  = "Latência Média das APIs (ms)"
      row    = 4
      column = 7
      width  = 6
      height = 3

      nrql_query {
        account_id = var.newrelic_account_id
        query      = "SELECT average(duration) * 1000 AS 'Latência (ms)' FROM Transaction WHERE appName = '${var.newrelic_app_name}' TIMESERIES 5 minutes SINCE 3 hours ago"
      }
    }

    widget_table {
      title  = "Erros e Falhas por Endpoint"
      row    = 7
      column = 1
      width  = 6
      height = 3

      nrql_query {
        account_id = var.newrelic_account_id
        query      = "SELECT count(*) FROM TransactionError WHERE appName = '${var.newrelic_app_name}' FACET request.uri, error.message SINCE 7 days ago LIMIT 20"
      }
    }

    widget_billboard {
      title  = "Uptime do Healthcheck (últimos 5min)"
      row    = 7
      column = 7
      width  = 6
      height = 3

      nrql_query {
        account_id = var.newrelic_account_id
        # O readinessProbe do K8s bate em /q/health/ready a cada 10s — a
        # transação é nomeada "q//health/ready" pelo agent (rota do
        # SmallRye Health, não um recurso JAX-RS comum, então request.uri
        # não fica populado aqui; usamos o nome da transação em vez disso).
        query = "SELECT filter(count(*), WHERE name LIKE '%health/ready%' AND httpResponseCode = '200') / filter(count(*), WHERE name LIKE '%health/ready%') * 100 AS 'Uptime %' FROM Transaction WHERE appName = '${var.newrelic_app_name}' SINCE 5 minutes ago"
      }
    }
  }
}

output "newrelic_dashboard_url" {
  description = "Link direto pro dashboard da Oficina API no New Relic."
  value       = newrelic_one_dashboard.oficina.permalink
}
