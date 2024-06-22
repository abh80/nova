FROM gradle:8.4.0-jdk17-jammy

COPY ./ci/scripts /scripts
RUN chmod +x /scripts/*
# UPDATE DEPENDENCIES
RUN apt-get update && apt-get upgrade -y && apt-get install -y curl wget unzip zip jq git

# INSTALL TERRAFORM
RUN /scripts/install_tf
RUN /scripts/install_coursier_and_scalafmt

# INSTALL postgresql
RUN apt-get install -y postgresql postgresql-client

ARG POSTGRES_USER=postgres
ARG POSTGRES_PASSWORD=postgres


USER postgres

RUN service postgresql start \
    && psql -U postgres -c "ALTER USER postgres WITH ENCRYPTED PASSWORD '${POSTGRES_PASSWORD}';" \
    && service postgresql stop


USER root

# INSTALL VAULT
RUN wget -O /tmp/vault.zip https://releases.hashicorp.com/vault/1.17.0/vault_1.17.0_linux_amd64.zip \
    && unzip -j /tmp/vault.zip vault -d /usr/local/bin \
    && chmod +x /usr/local/bin/vault \
    && rm /tmp/vault.zip \
    && vault --version