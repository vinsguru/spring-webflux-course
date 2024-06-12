-- it can be part of the docker-compose init sql.

CREATE TABLE customer (
   id SERIAL PRIMARY KEY,
   name VARCHAR(255),
   email VARCHAR(255)
);

INSERT INTO customer (name, email)
SELECT 'customer' || num, 'customer' || num || '@gmail.com'
FROM generate_series(1, 10000000) AS num;