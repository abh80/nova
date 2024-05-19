terraform {
  required_providers {
    vault = {
      source  = "hashicorp/vault"
      version = "4.2.0"
    }
  }
}
variable "token" {
  type = string
}

provider "vault" {
  address = "http://127.0.0.1:8200"
  token   = var.token
}
resource "vault_policy" "admins" {
  name   = "admins"
  policy = file("policies/admins-policy.hcl")
}
resource "vault_policy" "app-nova" {
  name   = "app-nova"
  policy = <<EOH
            path "database/static-creds/app-nova" {
            capabilities = [ "read" ]
            }
  EOH
}
resource "vault_policy" "database-admin" {
  name   = "database-admin"
  policy = file("policies/database-admin-policy.hcl")
}

resource "vault_mount" "ssh" {
  type = "ssh"
  path = "ssh-mount"
}

resource "vault_ssh_secret_backend_ca" "ca_pair" {
  backend              = vault_mount.ssh.path
  generate_signing_key = true
}

resource "vault_ssh_secret_backend_role" "ssh_role" {
  backend                 = vault_mount.ssh.path
  key_type                = "ca"
  name                    = "ssh-role"
  allow_user_certificates = true
  algorithm_signer        = "rsa-sha2-256"
  allowed_users           = "ubuntu"
  allowed_extensions      = "permit-pty,permit-port-forwarding"
  default_user            = "ubuntu"
  ttl                     = "30m0s"
}
resource "vault_mount" "db" {
  path = "database"
  type = "database"
}
resource "vault_database_secret_backend_connection" "psql_db_nova" {
  backend = vault_mount.db.path
  name = "main-db"
  allowed_roles     = ["app-nova", "database-admin"]
  verify_connection = true
  plugin_name = "postgresql-database-plugin"
  postgresql {
    username          = "nova"
    password          = "my_password"
    connection_url    = "postgresql://{{username}}:{{password}}@11.0.0.3:5432/main?sslmode=disable"
  }
}

resource "vault_database_secret_backend_static_role" "period_role" {
  backend             = vault_mount.db.path
  name                = "app-nova"
  db_name             = vault_database_secret_backend_connection.psql_db_nova.name
  username            = vault_database_secret_backend_connection.psql_db_nova.postgresql[0].username
  rotation_period     = "3600"
  rotation_statements = ["ALTER USER \"{{name}}\" WITH PASSWORD '{{password}}';"]
}