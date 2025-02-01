#!/bin/bash -xeu

# load backup from primary instance
pg_basebackup -h db_primary -p 5432 -D ${PGDATA} -S my_replication_slot --progress -X stream -U replicator -Fp -R || :

# start postgres
/usr/local/bin/docker-entrypoint.sh postgres