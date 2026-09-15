variable "db_admin_password" {
  description = "Senha do administrador do Azure Database for PostgreSQL Flexible Server. Nunca definir um valor aqui — fornecer via TF_VAR_db_admin_password ou um arquivo *.tfvars (gitignored)."
  type        = string
  sensitive   = true

  validation {
    condition     = length(var.db_admin_password) >= 12
    error_message = "A senha do banco deve ter pelo menos 12 caracteres."
  }
}

variable "newrelic_account_id" {
  description = "ID da conta New Relic (Account Settings > Account ID em one.newrelic.com)."
  type        = string
}

variable "newrelic_api_key" {
  description = "User API Key do New Relic (começa com 'NRAK-') — usada pelo provider Terraform pra criar dashboards e alertas via NerdGraph. NÃO é a license key do agent (essa vai só no k8s Secret). Nunca definir um valor aqui — fornecer via TF_VAR_newrelic_api_key ou um arquivo *.tfvars (gitignored)."
  type        = string
  sensitive   = true
}

variable "newrelic_app_name" {
  description = "Nome da aplicação como reportado ao New Relic pelo Java Agent — precisa bater com NEW_RELIC_APP_NAME em k8s/app/configMap.yaml, senão as queries do dashboard/alertas não encontram dado nenhum."
  type        = string
  default     = "Oficina API (AKS)"
}

variable "alert_notification_email" {
  description = "E-mail que recebe os alertas de falha da Oficina API. Deixe vazio para não criar canal de notificação (só a policy/condition ficam provisionadas, sem ninguém sendo avisado)."
  type        = string
  default     = ""
}

variable "jwt_private_key_pem" {
  description = "Conteúdo do mesmo privateKey.pem usado pela API (src/main/resources/privateKey.pem), para a Function de autenticação por CPF assinar tokens compatíveis. Nunca definir um valor aqui — fornecer via TF_VAR_jwt_private_key_pem ou um arquivo *.tfvars (gitignored)."
  type        = string
  sensitive   = true

  validation {
    condition     = length(var.jwt_private_key_pem) > 0
    error_message = "jwt_private_key_pem não pode ser vazio."
  }
}
