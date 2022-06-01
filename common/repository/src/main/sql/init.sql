CREATE SEQUENCE IF NOT EXISTS checker_id_seq;

CREATE TABLE IF NOT EXISTS checkers (
    id         INTEGER NOT NULL,
    dockerfile VARCHAR(255) NOT NULL,

    PRIMARY KEY (id)
);

CREATE SEQUENCE IF NOT EXISTS task_id_seq;

CREATE TABLE IF NOT EXISTS tasks (
    id             INTEGER NOT NULL,
    name           VARCHAR(255) NOT NULL,
    published_date TIMESTAMP NOT NULL,
    description    VARCHAR(255) NOT NULL,
    deadline_date  TIMESTAMP NOT NULL,
    checker_id     INTEGER NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (checker_id) REFERENCES checkers (id)
);

CREATE SEQUENCE IF NOT EXISTS submission_feedback_id_seq;

CREATE TABLE IF NOT EXISTS submission_feedbacks (
    id          INTEGER NOT NULL,
    verdict     VARCHAR(255) NOT NULL,
    comments    VARCHAR(1023) NOT NULL,

    PRIMARY KEY(id),
    CHECK (verdict IN ('yes', 'no'))
);

CREATE SEQUENCE IF NOT EXISTS submission_id_seq;

CREATE TABLE IF NOT EXISTS submissions (
    id              INTEGER NOT NULL,
    task_id         INTEGER NOT NULL,
    date            TIMESTAMP NOT NULL,
    result_id       INTEGER,
    repository_url  VARCHAR(255) NOT NULL,

    PRIMARY KEY(id),
    FOREIGN KEY(task_id) REFERENCES tasks (id),
    FOREIGN KEY(result_id) REFERENCES submission_feedbacks (id)
);
