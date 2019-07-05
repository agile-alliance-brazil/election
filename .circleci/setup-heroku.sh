#!/bin/bash
set -e

git remote add heroku https://git.heroku.com/aab-election.git
curl https://cli-assets.heroku.com/install.sh | sh

cat > ~/.netrc << EOF
machine api.heroku.com
  login $HEROKU_LOGIN
  password $HEROKU_API_KEY
machine git.heroku.com
  login $HEROKU_LOGIN
  password $HEROKU_API_KEY
EOF

# Add heroku.com to the list of known hosts
ssh-keyscan -H heroku.com >> ~/.ssh/known_hosts
