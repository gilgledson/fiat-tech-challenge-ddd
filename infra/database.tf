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
  zone                   = "1"
}

resource "azurerm_postgresql_flexible_server_firewall_rule" "allow_azure" {
  name             = "AllowAzureServices"
  server_id        = azurerm_postgresql_flexible_server.oficina_db.id
  start_ip_address = "0.0.0.0"
  end_ip_address   = "0.0.0.0"
}

resource "azurerm_postgresql_flexible_server_firewall_rule" "allow_all" {
  name             = "AllowAll"
  server_id        = azurerm_postgresql_flexible_server.oficina_db.id
  start_ip_address = "0.0.0.0"
  end_ip_address   = "255.255.255.255"
}