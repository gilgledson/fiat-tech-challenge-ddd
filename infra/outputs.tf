output "storage_account_name" {
  description = "Nome da Storage Account criada para as assinaturas de orçamento."
  value       = azurerm_storage_account.oficina_storage.name
}

output "storage_container_name" {
  description = "Nome do container de blobs onde as assinaturas são salvas."
  value       = azurerm_storage_container.assinaturas.name
}

output "storage_connection_string" {
  description = "Connection string da Storage Account. Usar como valor do secret AZURE_STORAGE_CONNECTION_STRING (GitHub Secrets / k8s Secret) — nunca commitar o valor real."
  value       = azurerm_storage_account.oficina_storage.primary_connection_string
  sensitive   = true
}
