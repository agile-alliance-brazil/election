CREATE TABLE clock_runs (
  id           SERIAL PRIMARY KEY NOT NULL,
  startdate    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  enddate      TIMESTAMPTZ,
  report       TEXT
);
