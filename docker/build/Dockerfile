FROM amazoncorretto:17

WORKDIR app/

RUN yum install -y dos2unix

ENTRYPOINT bash -c 'rm -f build/ready && \
                    dos2unix gradlew && \
                    ./gradlew :api:build :runner:build && \
                    touch build/ready'
