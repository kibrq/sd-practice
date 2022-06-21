#!/usr/bin/env bash

file="common/rabbitmq/src/main/resources/rabbitmq-prod.properties"
wait=docker/scripts/wait-for-it.sh

while IFS='=' read -r key value; do
    key=$(echo "$key" | tr '.' '_')
    eval "$key"="$value"
done <"$file"

# shellcheck disable=SC2154
bash $wait -t 30 "$rabbitmq_host:$rabbitmq_port"
