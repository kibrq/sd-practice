FROM ubuntu:focal

RUN apt update
RUN apt install -y curl zip unzip

RUN curl -s "https://get.sdkman.io" | bash
RUN bash -c 'source "$HOME/.sdkman/bin/sdkman-init.sh" && \
                sdk install java && \
                sdk install kotlin'

RUN apt install -y erlang

WORKDIR app/

ENTRYPOINT bash -c 'rm -f build/ready && \
                    source "$HOME/.sdkman/bin/sdkman-init.sh" && \
                    ./gradlew :api:build && ./gradlew :runner:build \
                    touch build/ready'
