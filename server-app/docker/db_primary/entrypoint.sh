#!/bin/bash -xeu

# start postgres
/usr/local/bin/docker-entrypoint.sh postgres \
  -c log_destination=stderr \
  -c log_statement=all \
  -c "hba_file=/etc/postgresql/pg_hba.conf"