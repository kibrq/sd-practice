FROM ubuntu:focal

RUN apt update
RUN apt install -y curl zip unzip

RUN curl -s "https://get.sdkman.io" | bash
RUN bash -c 'source "$HOME/.sdkman/bin/sdkman-init.sh" && \
                sdk install java && \
                sdk install kotlin'

WORKDIR app/

ENTRYPOINT bash -c 'rm -rf build && \
                    source "$HOME/.sdkman/bin/sdkman-init.sh" && \
                    ./gradlew build && \
                    touch build/ready'
