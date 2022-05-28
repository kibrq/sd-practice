#!/usr/bin/env bash

exit_code=6
while [ ! $exit_code == 52 ]; do
    curl database:5432
    exit_code=$?
    sleep 1;
done;
