FROM postgres:latest

ARG ANSIBLE_PASSWORD="test"

RUN apt update && apt install  openssh-server sudo python3 -y
RUN sed -i 's/#PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config && \
    sed -i 's/#PasswordAuthentication yes/PasswordAuthentication no/' /etc/ssh/sshd_config && \
    sed -i 's/#AuthorizedKeysFile/AuthorizedKeysFile/' /etc/ssh/sshd_config

RUN mkdir -p /root/.ssh
COPY .ssh/psql_key.pub /root/.ssh/authorized_keys
COPY ./start_services.sh /root/start_services.sh
RUN chmod +x /root/start_services.sh
RUN chmod 600 /root/.ssh/authorized_keys
RUN chmod 700 /root/.ssh
RUN service ssh start

EXPOSE 22
ENTRYPOINT ["/root/start_services.sh"]
CMD ["postgres"]