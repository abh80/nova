terraform {
  required_providers {
    vault = {
      source  = "hashicorp/vault"
      version = "2.20.0"
    }
    docker = {
      source = "kreuzwerker/docker"
    }
  }
}