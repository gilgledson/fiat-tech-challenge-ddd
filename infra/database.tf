resource "azurerm_resource_group" "oficina_rg" {
  name     = "oficina-resources"
  location = "Brazil South"
}

resource "azurerm_postgresql_flexible_server" "oficina_db" {
  name                   = "oficina-postgres-server"
  resource_group_name    = azurerm_resource_group.oficina_rg.name
  location               = azurerm_resource_group.oficina_rg.location
  version                = "13"
  administrator_login    = "adminuser"
  administrator_password = "Password123!" # Em produção, use o Key Vault!
  sku_name               = "B_Standard_B1ms"
}