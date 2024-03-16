resource "docker_image" "postgres" {
  name = "postgres-custom:latest"
  build {
    context   = "${path.module}/docker/postgresql"
    build_args = {
      ANSIBLE_PASSWORD = var.ansible_ssh_password
    }
  }
}
resource "docker_container" "postgresql-instance-main" {
  name  = "postgresql-instance-main"
  image = docker_image.postgres.image_id
  ports {
    internal = 5432
    external = 54321
  }
  ports {
    internal = 22
    external = 22
  }

  env = [
    "POSTGRES_USER=${var.postgres_user}",
    "POSTGRES_PASSWORD=${var.postgres_password}",
    "PGDATA=/var/lib/postgresql/data/pgdata"
  ]
  volumes {
    volume_name    = docker_volume.postgresql-instance-main.name
    container_path = "/var/lib/postgresql/data"
    host_path      = "/mnt/data/postgresql-instance-main"
    read_only      = false
  }
}

resource "docker_volume" "postgresql-instance-main" {
  name = "postgresql-instance-main-storage"
}