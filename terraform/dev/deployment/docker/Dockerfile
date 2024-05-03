FROM postgres
RUN apt-get update && apt-get install -y \
    curl openssh-server && mkdir -p /etc/vault-ssh-helper.d && \
    mkdir -p /usr/local/bin

RUN echo "TrustedUserCAKeys /etc/ssh/trusted-user-ca-keys.pem" >> /etc/ssh/sshd_config
RUN echo "PasswordAuthentication no" >> /etc/ssh/sshd_config

# create a script which runs every 5 minutes to update the public key, not in build process but when container runs
RUN echo "while true; do curl -o /etc/ssh/trusted-user-ca-keys.pem http://11.0.0.2:8200/v1/ssh-mount/public_key; sleep 300; done" > /usr/local/bin/update-public-key.sh
RUN chmod +x /usr/local/bin/update-public-key.sh
RUN curl -o /etc/ssh/trusted-user-ca-keys.pem http://127.0.0.1:8200/v1/ssh-mount/public_key

RUN service ssh start
RUN useradd -ms /bin/bash ubuntu

RUN usermod -aG sudo ubuntu
COPY entrypoint.sh /usr/local/bin/entrypoint.sh
ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
CMD ["postgres"]