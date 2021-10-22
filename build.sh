#!/usr/bin/env bash
set -e
# set -x # Uncomment to debug

MY_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

cd "${MY_DIR}"

"${MY_DIR}/setup.sh"
"${MY_DIR}/bin/lein" ring uberjar
