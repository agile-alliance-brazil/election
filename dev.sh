#!/usr/bin/env bash
set -e

MY_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

cd ${MY_DIR}

${MY_DIR}/setup.sh

ps xau | grep livereload | grep -v grep | awk '{print $2}' | xargs kill

if [[ ! -f ${MY_DIR}/.env ]]; then
  echo "Setting up initial .env"
  printf "DEV=true\n\
EMAIL_SENDER=brazil@agilealliance.org\n\
AWS_HOST=email-smtp.us-east-1.amazonaws.com\n\
AWS_USER=smtp_user\n\
AWS_PASS=smtp_password\n\
AWS_IAM=smtp_iam_identifier\n\
HOST=http://localhost:5000\n" > ${MY_DIR}/.env
fi

${MY_DIR}/scripts/start_postgres.sh

echo "Starting servers..."
${MY_DIR}/bin/lein cooper
