resource "docker_network" "main-net" {
  driver      = "bridge"
  name        = "flowops-net-${random_uuid.net_id.result}"
  ipam_driver = "default"
  ipam_config {
    gateway = "11.0.0.1"
    subnet  = "11.0.0.0/24"
  }
}

resource "random_uuid" "net_id" {}