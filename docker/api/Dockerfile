FROM ubuntu:focal

RUN apt update
RUN apt install -y curl zip unzip

RUN curl -s "https://get.sdkman.io" | bash
RUN bash -c 'source "$HOME/.sdkman/bin/sdkman-init.sh" && \
                sdk install java && \
                sdk install kotlin'

WORKDIR app/

ENTRYPOINT bash -c 'docker/scripts/monitor-database.sh && \
                        docker/scripts/monitor-rabbitmq.sh && \
                        docker/scripts/monitor-build.sh && \
                        source "$HOME/.sdkman/bin/sdkman-init.sh" && \
                        java -jar -Dspring.profiles.active=prod api/build/libs/api-0.0.1-SNAPSHOT.jar'
