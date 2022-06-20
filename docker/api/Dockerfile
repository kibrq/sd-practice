FROM amazoncorretto:17

WORKDIR app/

RUN yum install -y dos2unix

ENTRYPOINT bash -c 'find docker/scripts -type f -print0 | xargs -0 dos2unix && \
                        docker/scripts/monitor-database.sh && \
                        docker/scripts/monitor-rabbitmq.sh && \
                        docker/scripts/monitor-build.sh && \
                        java -jar -Dspring.profiles.active=prod api/build/libs/api-0.0.1-SNAPSHOT.jar'
