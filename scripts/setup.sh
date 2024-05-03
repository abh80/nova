#!/usr/bin/env bash
echo "Welcome to Flow, the ultimate development environment!"
echo "Setting up dependencies for Ansible, Docker, and Terraform..."
# Install dependencies
echo "Updating dependencies"
apt-get update
## ---- Ansible ----
echo "Installing Ansible"
apt-get install -y software-properties-common python-software-properties python-setuptools python-pip
apt-add-repository -y ppa:ansible/ansible
apt-get update
apt-get install -y ansible

## ---- Docker ----
echo "Installing Docker"
curl "https://get.docker.com" | bash
usermod -aG docker vagrant

## ---- Terraform ----
echo "Installing Terraform"
wget https://releases.hashicorp.com/terraform/1.7.5/terraform_1.7.5_linux_amd64.zip
unzip terraform_1.7.5_linux_amd64.zip
mv terraform /usr/local/bin/
rm terraform_1.7.5_linux_amd64.zip
### test terraform
echo "Terraform version $(terraform --version) installed successfully!"

echo "Dependencies installed successfully!"

echo "Setting up the environment..."
# Generate SSH key
echo "Generating SSH key..."
ssh-keygen -t rsa -N "" -f /home/vagrant/.ssh/id_rsa
