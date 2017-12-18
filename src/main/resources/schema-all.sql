DROP TABLE access IF EXISTS;

CREATE TABLE access  (
    access_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    dt_access TIMESTAMP,
    ip_address VARCHAR(200),
    request VARCHAR(200),
    status VARCHAR(200),
    user_agent VARCHAR(200),
);
