#!/bin/bash

# Start SSH daemon
/usr/sbin/sshd -D &

exec /bin/bash /usr/local/bin/docker-entrypoint.sh "$@"