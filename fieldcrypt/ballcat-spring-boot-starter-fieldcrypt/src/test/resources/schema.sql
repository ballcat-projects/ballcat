DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user (
  id BIGINT PRIMARY KEY,
  name VARCHAR(64),
  mobile VARCHAR(255),
  email VARCHAR(255),
  address VARCHAR(255)
);
