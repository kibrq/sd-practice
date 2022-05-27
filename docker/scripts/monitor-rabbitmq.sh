#!/usr/bin/env bash

while [ -z $(curl -s rabbitmq:15672) ]; do sleep 1; done;
