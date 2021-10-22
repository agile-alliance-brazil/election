#!/usr/bin/env bash
set -e
set -x # Uncomment to debug

DB_NAME=election

if [ -n "$(psql --version &> /dev/null)" ]; then
  echo "Installing PostgreSQL"
  if [ -n "$(command -v brew)" ]; then
    brew install postgresql@9.4 > /dev/null
  elif [ -n "$(command -v apt-get)" ]; then
    apt-get install -y postgresql-9.4 > /dev/null
  else
    echo "Only apt-get or brew supported for PostgreSQL installation, either install manually or fix this script for your system" && exit 1
  fi
fi

MY_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

TMP_FOLDER="${MY_DIR}/../tmp"
POSTGRES_FOLDER="${TMP_FOLDER}/postgres"

if [ ! -d "${POSTGRES_FOLDER}" ]; then
  echo "Setting up postgres data folder"
  (mkdir -p "${POSTGRES_FOLDER}" &> /dev/null) && (initdb "${POSTGRES_FOLDER}" &> /dev/null) || (echo "DB folder already set up" &> /dev/null)
fi

if [ -z $(ps xau | grep bin/postgres | grep -v grep | grep -v java | grep -v psql) ]; then
  echo "Starting postgres..."
  pg_ctl -D "${POSTGRES_FOLDER}" -l "${POSTGRES_FOLDER}/server.log" start
fi

sleep 1

DATABASE_URL="postgresql://127.0.0.1:5432/${DB_NAME}"
if [ -z $(cat ${MY_DIR}/../.env | grep DATABASE_URL) ]; then
  printf "\nDATABASE_URL=${DATABASE_URL}\n" >> "${MY_DIR}/../.env"
fi

(createdb "${DB_NAME}" &> /dev/null && echo "Created DB ${DB_NAME}") || (echo "Database ${DB_NAME} already exists" &> /dev/null)

echo "Running migrations"
DATABASE_URL="${DATABASE_URL}" "${MY_DIR}/../bin/lein" migrate
