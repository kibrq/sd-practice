#!/usr/bin/env bash

while [ -z "$(curl -s rabbitmq-container:15672)" ]; do echo "$(curl rabbitmq-container:15672)"  && sleep 1; done;
