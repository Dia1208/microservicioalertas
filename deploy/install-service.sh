#!/usr/bin/env bash
set -euo pipefail

if [[ "$(id -u)" -ne 0 ]]; then
    echo "Ejecuta este script con sudo" >&2
    exit 1
fi

install -m 644 /home/ubuntu/alertas-zaror.service /etc/systemd/system/alertas-zaror.service
systemctl daemon-reload
systemctl enable --now alertas-zaror.service
systemctl is-active alertas-zaror.service
