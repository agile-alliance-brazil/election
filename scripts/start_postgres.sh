#!/usr/bin/env bash
set -e

DB_NAME=election

(psql --version &> /dev/null) || ( (brew --version &> /dev/null) && echo "Installing PostgreSQL" && (brew install postgresql > /dev/null) )
(psql --version &> /dev/null) || ( (apt-get --version &> /dev/null) && echo "Installing PostgreSQL" && (apt-get install -y postgresql-9.4 > /dev/null) )

MY_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

TMP_FOLDER="${MY_DIR}/../tmp"
POSTGRES_FOLDER=${TMP_FOLDER}/postgres

if [[ ! -d ${POSTGRES_FOLDER} ]]; then
  echo "Setting up postgres data folder"
  (mkdir -p ${POSTGRES_FOLDER} &> /dev/null) && (initdb ${POSTGRES_FOLDER} &> /dev/null) || (echo "DB folder already set up" &> /dev/null)
fi

if [[ -z `ps xau | grep postgresql | grep -v grep | grep -v java` ]]; then
  echo "Starting postgres"
  pg_ctl -D ${POSTGRES_FOLDER} -l ${POSTGRES_FOLDER}/server.log stop
fi

sleep 1

DATABASE_URL="postgresql://127.0.0.1:5432/${DB_NAME}"
if [[ -z `cat ${MY_DIR}/../.env | grep DATABASE_URL` ]]; then
  printf "\nDATABASE_URL=${DATABASE_URL}\n" >> ${MY_DIR}/../.env
fi

(createdb ${DB_NAME} &> /dev/null && echo "Created DB ${DB_NAME}") || (echo "Database ${DB_NAME} already exists" &> /dev/null)

echo "Running migrations"
DATABASE_URL=${DATABASE_URL} ${MY_DIR}/../bin/lein migrate
