#!/bin/bash

set -eu -o pipefail

until nc -z "${POSTGRES_SEEDS%%,*}" "${DB_PORT}"; do
  echo 'Waiting for PostgreSQL to startup.'
  sleep 1
done

echo 'PostgreSQL started.'

POSTGRES_VERSION_DIR=v12
DEFAULT_SCHEMA_DIR=/etc/temporal/schema/postgresql/${POSTGRES_VERSION_DIR}/temporal/versioned
VISIBILITY_SCHEMA_DIR=/etc/temporal/schema/postgresql/${POSTGRES_VERSION_DIR}/visibility/versioned

export SQL_PASSWORD="${POSTGRES_PWD}"
temporal-sql-tool \
  --plugin "${DB}" \
  --ep "${POSTGRES_SEEDS}" \
  -u "${POSTGRES_USER}" \
  -p "${DB_PORT}" \
  --db "${DBNAME}" \
  setup-schema -v 0.0 || true
temporal-sql-tool \
  --plugin "${DB}" \
  --ep "${POSTGRES_SEEDS}" \
  -u "${POSTGRES_USER}" \
  -p "${DB_PORT}" \
  --db "${DBNAME}" \
  update-schema -d "${DEFAULT_SCHEMA_DIR}"

export SQL_PASSWORD="${VISIBILITY_POSTGRES_PWD}"
temporal-sql-tool \
  --plugin "${DB}" \
  --ep "${POSTGRES_SEEDS}" \
  -u "${VISIBILITY_POSTGRES_USER}" \
  -p "${DB_PORT}" \
  --db "${VISIBILITY_DBNAME}" \
  setup-schema -v 0.0 || true
temporal-sql-tool \
  --plugin "${DB}" \
  --ep "${POSTGRES_SEEDS}" \
  -u "${VISIBILITY_POSTGRES_USER}" \
  -p "${DB_PORT}" \
  --db "${VISIBILITY_DBNAME}" \
  update-schema -d "${VISIBILITY_SCHEMA_DIR}"

exec /etc/temporal/entrypoint.sh autosetup
