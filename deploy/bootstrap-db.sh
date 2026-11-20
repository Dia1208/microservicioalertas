#!/usr/bin/env bash
set -euo pipefail

if [[ "$(id -u)" -ne 0 ]]; then
    echo "Ejecuta este script con sudo" >&2
    exit 1
fi

db_pass="$(openssl rand -hex 24)"
mysql <<SQL
CREATE DATABASE IF NOT EXISTS db_alertas_zaror CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'alertas_app'@'localhost' IDENTIFIED BY '${db_pass}';
ALTER USER 'alertas_app'@'localhost' IDENTIFIED BY '${db_pass}';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, INDEX, REFERENCES
ON db_alertas_zaror.* TO 'alertas_app'@'localhost';
SQL

install -d -m 700 /etc/alertas-zaror
cat > /etc/alertas-zaror/app.env <<ENV
DB_URL=jdbc:mysql://localhost:3306/db_alertas_zaror
DB_USERNAME=alertas_app
DB_PASSWORD=${db_pass}
SERVER_ADDRESS=127.0.0.1
ENV
chmod 600 /etc/alertas-zaror/app.env
echo "Base de datos y usuario configurados; credenciales en /etc/alertas-zaror/app.env"
