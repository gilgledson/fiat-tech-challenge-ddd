# Armazenamento das assinaturas de aceite de orçamento. Antes ficavam no disco
# local do pod (efêmero: some a cada restart/redeploy e não é compartilhado
# entre réplicas do HPA) — agora vão pro Azure Blob Storage.

# Nome de Storage Account precisa ser globalmente único em todo o Azure,
# então usamos um sufixo aleatório em vez de fixar um nome.
resource "random_string" "storage_suffix" {
  length  = 6
  special = false
  upper   = false
}

resource "azurerm_storage_account" "oficina_storage" {
  name                     = "oficinasign${random_string.storage_suffix.result}"
  resource_group_name      = azurerm_resource_group.oficina_rg.name
  location                 = azurerm_resource_group.oficina_rg.location
  account_tier             = "Standard"
  account_replication_type = "LRS"
  min_tls_version          = "TLS1_2"
}

resource "azurerm_storage_container" "assinaturas" {
  name                  = "assinaturas"
  storage_account_name  = azurerm_storage_account.oficina_storage.name
  container_access_type = "private"
}
