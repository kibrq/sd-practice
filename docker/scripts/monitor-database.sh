#!/usr/bin/env bash

while [ -z $(curl database:5432) ]; do sleep 1; done;
