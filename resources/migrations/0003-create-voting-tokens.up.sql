CREATE TABLE voting_tokens (
  id           SERIAL PRIMARY KEY,
  electionid   INTEGER,
  token        VARCHAR(255),
  used         BOOLEAN
);
