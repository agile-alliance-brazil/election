CREATE TABLE voting_tokens (
  id           SERIAL PRIMARY KEY NOT NULL,
  electionid   INTEGER NOT NULL,
  token        VARCHAR(255) NOT NULL,
  used         BOOLEAN NOT NULL DEFAULT FALSE
);
CREATE INDEX voting_tokens_electionid_idx ON voting_tokens (electionid);
CREATE INDEX voting_tokens_token_idx ON voting_tokens (token);
CREATE INDEX voting_tokens_used_idx ON voting_tokens (used);
