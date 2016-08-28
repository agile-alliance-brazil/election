CREATE TABLE voters (
  id           SERIAL PRIMARY KEY,
  electionid   INTEGER,
  fullname     VARCHAR(255),
  email        VARCHAR(255)
);
