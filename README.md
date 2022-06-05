# Hwproj

## To build and run everything use

```bash
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

## Contributors

Kirill Brilliantov [kibrq](https://github.com/kibrq)

Maxim Sukhodolskii [maxuh14](https://github.com/maxuh14)

Ruslan Salkaev [salkaevruslan](https://github.com/salkaevruslan)
