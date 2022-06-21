#!/usr/bin/env bash

file="common/repository/src/main/resources/database-prod.properties"
wait=docker/scripts/wait-for-it.sh

while IFS='=' read -r key value; do
    key=$(echo "$key" | tr '.' '_' | tr -d '\r')
    value=$(echo "$value" | tr -d '\r')
    eval "$key"="$value"
done <"$file"

# shellcheck disable=SC2154
bash $wait -t 0 "$db_host:$db_port"
