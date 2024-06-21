resource "docker_container" "vault" {
  image = "hashicorp/vault"
  name  = "vault"
  volumes {
    container_path = "/vault/config"
    host_path      = "${path.cwd}/vault/config"
    read_only      = false
  }
  volumes {
    container_path = "/vault/path"
    read_only      = true
  }
  networks_advanced {
    name         = docker_network.main-net.id
    ipv4_address = "11.0.0.2"
  }
  ports {
    internal = 8200
    external = 8200
  }
}