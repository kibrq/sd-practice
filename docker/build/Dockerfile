FROM amazoncorretto:17

WORKDIR app/

RUN yum install -y dos2unix

ENTRYPOINT bash -c 'rm -f build/ready && \
                    find docker/scripts -type f -print0 | xargs -0 dos2unix && \
                    dos2unix gradlew && \
                    ./gradlew :api:build :runner:build && \
                    touch build/ready'
