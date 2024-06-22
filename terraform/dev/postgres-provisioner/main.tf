terraform {
  required_providers {
    postgresql = {
      source  = "cyrilgdn/postgresql"
      version = "1.22.0"
    }
  }
}
variable "postgres_db_password" {
  type = string
}

variable "postgres_host" {
  type    = string
  default = "127.0.0.1"
}

variable "postgres_username" {
  type    = string
  default = "postgres"
}
provider "postgresql" {
  host     = var.postgres_host
  port     = 5432
  username = var.postgres_username
  password = var.postgres_db_password
  sslmode  = "disable"
}

resource "postgresql_database" "main" {
  name  = "main"
  owner = "postgres"
}

resource "postgresql_role" "nova_role" {
  name     = "nova"
  login    = true
  password = "my_password"
}

resource "postgresql_schema" "nova_schema" {
  name     = "nova"
  owner    = postgresql_role.nova_role.name
  database = postgresql_database.main.name
}
