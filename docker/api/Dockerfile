FROM ubuntu:focal

RUN apt update
RUN apt install -y curl zip unzip

RUN curl -s "https://get.sdkman.io" | bash
RUN bash -c 'source "$HOME/.sdkman/bin/sdkman-init.sh" && \
                sdk install java && \
                sdk install kotlin && \
                sdk install gradle'

WORKDIR app/

ENTRYPOINT bash -c 'source "$HOME/.sdkman/bin/sdkman-init.sh" && gradle :api:bootRun'
