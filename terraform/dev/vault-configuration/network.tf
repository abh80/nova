resource "docker_network" "main-net" {
  driver      = "bridge"
  name        = "main_net_1"
  ipam_driver = "default"
  ipam_config {
    gateway = "11.0.0.1"
    subnet  = "11.0.0.0/24"
  }
}