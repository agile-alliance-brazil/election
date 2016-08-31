CREATE TABLE voters (
  id           SERIAL PRIMARY KEY NOT NULL,
  electionid   INTEGER NOT NULL,
  fullname     VARCHAR(255) NOT NULL,
  email        VARCHAR(255) NOT NULL
);
CREATE INDEX voters_electionid_idx ON voters (electionid);
CREATE INDEX voters_fullname_idx ON voters (fullname);
CREATE INDEX voters_email_idx ON voters (email);
CREATE UNIQUE INDEX voters_electionid_email_idx ON voters (electionid, email);
