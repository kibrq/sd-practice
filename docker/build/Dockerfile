FROM amazoncorretto:17

WORKDIR app/

ENTRYPOINT bash -c 'rm -f build/ready && \
                    ./gradlew :api:build :runner:build && \
                    touch build/ready'
