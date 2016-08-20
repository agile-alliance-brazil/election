CREATE TABLE candidates (
  id           SERIAL PRIMARY KEY,
  electionid   INTEGER,
  fullname     VARCHAR(255),
  minibio      TEXT,
  email        VARCHAR(255),
  votecount    INTEGER
);
