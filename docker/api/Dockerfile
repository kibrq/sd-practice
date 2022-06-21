FROM amazoncorretto:17

WORKDIR app/

ENTRYPOINT bash -c 'docker/scripts/monitor-database.sh && \
                        docker/scripts/monitor-rabbitmq.sh && \
                        docker/scripts/monitor-build.sh && \
                        java -jar -Dspring.profiles.active=prod api/build/libs/api-0.0.1-SNAPSHOT.jar'
