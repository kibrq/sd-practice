CREATE SCHEMA IF NOT EXISTS public;

CREATE TABLE IF NOT EXISTS Checkers (
    id         VARCHAR(255) NOT NULL,
    dockerfile VARCHAR(255) NOT NULL,

    PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS Tasks (
    id             INTEGER NOT NULL,
    name           VARCHAR(255) NOT NULL,
    published_date TIMESTAMP NOT NULL,
    description    VARCHAR(255) NOT NULL,
    deadline_date  TIMESTAMP NOT NULL,
    checker_id     VARCHAR(255) NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (checker_id) REFERENCES Checkers (id)
);
