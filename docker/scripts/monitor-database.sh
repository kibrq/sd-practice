#!/usr/bin/env bash

file="common/repository/src/main/resources/database-prod.properties"
wait=docker/scripts/wait-for-it.sh

while IFS='=' read -r key value
do
    key=$(echo $key | tr '.' '_')
    eval ${key}=\${value}
done < "$file"

bash $wait "$db_host:$db_port"
