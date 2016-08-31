CREATE TABLE elections (
  id           SERIAL PRIMARY KEY NOT NULL,
  name         VARCHAR(255) NOT NULL,
  description  TEXT NOT NULL,
  createdat    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updatedat    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  startdate    TIMESTAMPTZ NOT NULL,
  enddate      TIMESTAMPTZ NOT NULL
);
CREATE INDEX elections_startdate_idx ON elections (startdate);
