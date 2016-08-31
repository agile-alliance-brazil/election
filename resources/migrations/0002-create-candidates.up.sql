CREATE TABLE candidates (
  id           SERIAL PRIMARY KEY NOT NULL,
  electionid   INTEGER NOT NULL,
  fullname     VARCHAR(255) NOT NULL,
  minibio      TEXT,
  email        VARCHAR(255) NOT NULL,
  votecount    INTEGER NOT NULL default 0,
  createdat    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updatedat    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX candidates_electionid_idx ON candidates (electionid);
CREATE INDEX candidates_fullname_idx ON candidates (fullname);
CREATE INDEX candidates_email_idx ON candidates (email);
CREATE UNIQUE INDEX candidates_electionid_email_idx ON candidates (electionid, email);
