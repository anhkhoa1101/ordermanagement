#!/bin/bash
# Khởi động PostgreSQL nhúng (nếu không truyền DB ngoài) rồi chạy ứng dụng.
set -euo pipefail

DB_NAME="${EMBEDDED_DB_NAME:-ordermanagement}"
DB_USER="${EMBEDDED_DB_USER:-app}"
DB_PASS="${EMBEDDED_DB_PASSWORD:-app}"

PG_STARTED=0
PG_VER=""

if [ -z "${SPRING_DATASOURCE_URL:-}" ]; then
  PG_VER="$(ls /usr/lib/postgresql | sort -V | tail -1)"
  echo ">> Không có SPRING_DATASOURCE_URL -> khởi động PostgreSQL ${PG_VER} nhúng trong container"

  pg_ctlcluster "$PG_VER" main start
  PG_STARTED=1

  for i in $(seq 1 60); do
    pg_isready -q -h 127.0.0.1 -p 5432 && break
    sleep 1
  done
  pg_isready -q -h 127.0.0.1 -p 5432 || { echo "PostgreSQL không khởi động được" >&2; exit 1; }

  psql_pg() { su postgres -c "psql -v ON_ERROR_STOP=1 -tA $*"; }

  if ! psql_pg "-c \"SELECT 1 FROM pg_roles WHERE rolname='${DB_USER}'\"" | grep -q 1; then
    psql_pg "-c \"CREATE ROLE ${DB_USER} LOGIN PASSWORD '${DB_PASS}'\""
  fi
  if ! psql_pg "-c \"SELECT 1 FROM pg_database WHERE datname='${DB_NAME}'\"" | grep -q 1; then
    psql_pg "-c \"CREATE DATABASE ${DB_NAME} OWNER ${DB_USER}\""
  fi

  export SPRING_DATASOURCE_URL="jdbc:postgresql://127.0.0.1:5432/${DB_NAME}"
  export SPRING_DATASOURCE_USERNAME="$DB_USER"
  export SPRING_DATASOURCE_PASSWORD="$DB_PASS"
fi

# Chạy app bằng user không phải root
setpriv --reuid=spring --regid=spring --init-groups \
  java ${JAVA_OPTS:-} -jar /app/app.jar &
APP_PID=$!

forward() { kill -TERM "$APP_PID" 2>/dev/null || true; }
trap forward TERM INT

set +e
wait "$APP_PID"
CODE=$?
# nếu wait bị ngắt bởi tín hiệu, đợi app tắt hẳn để lấy mã thoát thật
if kill -0 "$APP_PID" 2>/dev/null; then wait "$APP_PID"; CODE=$?; fi
trap - TERM INT

if [ "$PG_STARTED" = "1" ]; then
  pg_ctlcluster "$PG_VER" main stop -m fast || true
fi
exit "$CODE"
