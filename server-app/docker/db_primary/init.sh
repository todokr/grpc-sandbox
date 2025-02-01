#!/bin/sh -xeu

psql -v ON_ERROR_STOP=1 <<- EOT
  CREATE USER replicator WITH REPLICATION;
  SELECT pg_create_physical_replication_slot('my_replication_slot');
EOT