#!/usr/bin/env bash
set -e
# set -x # Uncomment to debug

if [ -z $( (uname -a | grep Darwin &> /dev/null) && command -v brew ) ]; then
  # TODO: check for ruby and exit if not available.
  echo "Installing brew..."
  /usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
fi

if [ -z "$(command -v curl)" ]; then
  echo "Installing curl..."
  if [ -n "$(command -v brew)" ]; then
    brew install curl > /dev/null
  elif [ -n "$(command -v apt-get)" ]; then
    apt-get install curl -y > /dev/null
  else
    echo "Only apt-get or brew supported for curl installation, either install manually or fix this script for your system" && exit 1
  fi
fi

if [ -z "$( ((command -v javac &> /dev/null) && javac -version 2>&1) | grep '1.8') " ]; then
  echo "Installing java 1.8..."
  if [ -n "$(command -v brew)" ]; then
    brew tap adoptopenjdk/openjdk
    brew install --cask adoptopenjdk/openjdk/adoptopenjdk8
    export JAVA_HOME=$(/usr/libexec/java_home -v"1.8")
  elif [ -n "$(command -v apt-get)" ]; then
    apt-get install python-software-properties
    add-apt-repository ppa:webupd8team/java
    apt-get update
    apt-get install -y oracle-java8-installer
  else
    echo "Only apt-get or brew supported for java installation, either install manually or fix this script for your system" && exit 1
  fi
fi

MY_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

cd "${MY_DIR}"

mkdir -p "${MY_DIR}/bin"
mkdir -p "${MY_DIR}/tmp"
mkdir -p "${MY_DIR}/lib"
if [ ! -f "${MY_DIR}/bin/lein" ]; then
  echo "Installing local lein..."
  curl --silent "https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein" > "${MY_DIR}/bin/lein"
fi
chmod +x "${MY_DIR}/bin/lein"
"${MY_DIR}/bin/lein" &> /dev/null
echo "Installing dependencies..."
"${MY_DIR}/bin/lein" deps &> /dev/null
