terraform {
  required_providers {
    postgresql = {
      source  = "cyrilgdn/postgresql"
      version = "1.22.0"
    }
  }
}
variable "password" {
  type = string
}
provider "postgresql" {
  host     = "127.0.0.1"
  port     = 5432
  username = "postgres"
  password = var.password
  sslmode = "disable"
}

resource "postgresql_database" "main" {
  name = "main"
  owner = "postgres"
}

resource "postgresql_role" "nova_role" {
  name     = "nova"
  login    = true
  password = "my_password"
}

resource "postgresql_schema" "nova_schema" {
  name  = "nova"
  owner = postgresql_role.nova_role.name
  database = postgresql_database.main.name
}
