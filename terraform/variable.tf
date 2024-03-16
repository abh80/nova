variable "postgres_user" {
  default = "root"
  type    = string
}
variable "postgres_password" {
  type = string
}
variable "ansible_ssh_password" {
    type = string
}