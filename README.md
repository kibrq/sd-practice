## Server

### To build and run everything use

```bash
docker-compose up
```

### Services running in docker:
* database — runs postgres on localhost:5432
* rabbitmq — runs rabbitmq server on localhost:5672
* api — runs server api on localhost:8080
* runner — runners who check submissions; connected to the api via rabbitmq

### API docs

http://localhost:8080/swagger-ui.html

## Client

### To install dependencies use

```bash
npm install --prefix hwproj-client
```

### To run client use
```bash
npm start --prefix hwproj-client
```

### The client will be up running on

http://localhost:3000/

## Contributors

Kirill Brilliantov [kibrq](https://github.com/kibrq)

Maxim Sukhodolskii [maxuh14](https://github.com/maxuh14)

Ruslan Salkaev [salkaevruslan](https://github.com/salkaevruslan)
