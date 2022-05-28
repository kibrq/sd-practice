#!/usr/bin/env bash

# prerequisites: git, make
# apt install -y curl git
# apt install -y build-essential libssl-dev automake make autoconf libncurses5-dev
# curl -O https://raw.githubusercontent.com/kerl/kerl/master/kerl

git clone https://github.com/erlang/otp.git
cd otp
git checkout maint-24    # current latest stable version
./configure
make
make install
