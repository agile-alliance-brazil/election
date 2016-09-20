CREATE TABLE elections (
  id                    SERIAL PRIMARY KEY NOT NULL,
  name                  VARCHAR(255) NOT NULL,
  description           TEXT NOT NULL,
  createdat             TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updatedat             TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  startdate             TIMESTAMPTZ NOT NULL,
  enddate               TIMESTAMPTZ NOT NULL,
  candidatestoelect     INTEGER NOT NULL DEFAULT 1,
  candidatestovoteon    INTEGER NOT NULL DEFAULT 1
);
CREATE INDEX elections_startdate_idx ON elections (startdate);
