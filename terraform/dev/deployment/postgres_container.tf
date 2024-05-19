resource "docker_image" "postgres" {
  name = "postgres:latest"
}
resource "docker_container" "postgresql-instance-main" {
  name  = "postgresql-instance-main"
  image = docker_image.postgres.image_id
  ports {
    internal = 5432
    external = 5432
  }

  env = [
    "POSTGRES_USER=postgres",
    "POSTGRES_PASSWORD=my_password",
    "PGDATA=/var/lib/postgresql/data/pgdata",
    "VAULT_ADDR=http://11.0.0.2:8200"
  ]
  volumes {
    volume_name    = docker_volume.postgresql-instance-main.name
    container_path = "/var/lib/postgresql/data"
    host_path      = "/mnt/data/postgresql-instance-main"
    read_only      = false
  }
  networks_advanced {
    name = "main_net_1"
    ipv4_address = "11.0.0.3"
  }
}

resource "docker_volume" "postgresql-instance-main" {
  name = "postgresql-instance-main-storage"
}
