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
variable "admin_password" {
  type = string
}
variable "apps_password" {
  type = string
}
variable "database_admin_password" {
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
resource "vault_policy" "apps" {
  name   = "apps"
  policy = file("policies/apps-policy.hcl")
}
resource "vault_policy" "database-admin" {
  name   = "database-admin"
  policy = file("policies/database-admin-policy.hcl")
}

resource "vault_auth_backend" "userpass" {
  type = "userpass"
}
resource "vault_generic_endpoint" "admin" {

  depends_on           = [vault_auth_backend.userpass]
  path                 = "auth/userpass/users/admin"
  ignore_absent_fields = true
  data_json            = <<EOT
          {
            "policies": ["admins"],
            "password": "${var.admin_password}"
          }
          EOT
}

resource "vault_generic_endpoint" "apps" {
  depends_on           = [vault_auth_backend.userpass]
  path                 = "auth/userpass/users/apps"
  ignore_absent_fields = true
  data_json            = <<EOT
          {
            "policies": ["apps"],
            "password": "${var.apps_password}"
          }
          EOT
}

resource "vault_generic_endpoint" "database-admin" {
  depends_on           = [vault_auth_backend.userpass]
  path                 = "auth/userpass/users/database-admin"
  ignore_absent_fields = true
  data_json            = <<EOT
          {
            "policies": ["database-admin"],
            "password": "${var.database_admin_password}"
          }
          EOT
}

resource "vault_mount" "ssh" {
  type = "ssh"
  path = "ssh-mount"
}

resource "vault_ssh_secret_backend_ca" "ca_pair" {
    backend = vault_mount.ssh.path
    generate_signing_key = true
}

resource "vault_ssh_secret_backend_role" "ssh_role" {
  backend  = vault_mount.ssh.path
  key_type = "ca"
  name     = "ssh-role"
  allow_user_certificates = true
  algorithm_signer = "rsa-sha2-256"
  allowed_users = "ubuntu"
  allowed_extensions = "permit-pty,permit-port-forwarding"
  default_user = "ubuntu"
  ttl = "30m0s"
}

resource "vault_database_secrets_mount" "psql_db" {
  path = "database"

  postgresql {
    plugin_name = "postgresql-database-plugin"
    name              = "main-db"
    username          = "postgres"
    password          = "my_password"
    connection_url    = "postgresql://{{username}}:{{password}}@11.0.0.3:5432?sslmode=disable"
    verify_connection = true
    allowed_roles     = ["apps", "database-admin", "root", "admins"]
  }
}
