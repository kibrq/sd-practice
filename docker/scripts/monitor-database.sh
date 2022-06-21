#!/usr/bin/env bash

file="common/repository/src/main/resources/database-prod.properties"
wait=docker/scripts/wait-for-it.sh

while IFS='=' read -r key value; do
    key=$(echo -n "$key" | tr '.' '_')
    eval "$key"="$value"
done <"$file"

# shellcheck disable=SC2154
bash $wait -t 30 "$db_host:$db_port"
