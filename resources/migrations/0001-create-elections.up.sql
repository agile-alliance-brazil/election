CREATE TABLE elections (
  id           SERIAL PRIMARY KEY,
  name         VARCHAR(255),
  description  TEXT,
  createdat    TIMESTAMPTZ,
  startdate    TIMESTAMPTZ,
  enddate      TIMESTAMPTZ
);
