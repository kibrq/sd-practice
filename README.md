# Hwproj

## To build and run everything on Linux and Mac use:

```bash
docker-compose up
```

## Windows - cmd:

```shell
echo PWD=%cd%>.env
docker-compose up
```

## Windows - PowerShell:

```shell
"PWD=$PWD" | Out-File -FilePath .env -Encoding ASCII
docker-compose up
```

### Services running in docker:

* database — runs postgres on localhost:5432
* rabbitmq — runs rabbitmq server on localhost:5672
* api — runs server api on localhost:8080
* runner — runners who check submissions; connected to the api via rabbitmq
* client — hwproj web interface

### Server

Api docs will be available at

http://localhost:8080/swagger-ui.html

### Client

The client will be up running at

http://localhost:3000/

### Manual testing

Sample Dockerfile for manual testing can be found in `integration-tests/src/test/resources/Dockerfile`.

## Contributors

Kirill Brilliantov [kibrq](https://github.com/kibrq)

Maxim Sukhodolskii [maxuh14](https://github.com/maxuh14)

Ruslan Salkaev [salkaevruslan](https://github.com/salkaevruslan)
