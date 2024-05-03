#!/bin/bash
/usr/local/bin/update-public-key.sh &
/usr/sbin/sshd -D &

exec /bin/bash /usr/local/bin/docker-entrypoint.sh "$@"
