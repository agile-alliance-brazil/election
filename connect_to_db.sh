#!/usr/bin/env bash
`cat .env | grep postgres | sed -e 's/[^=]*=\(.*\)/psql \1/'`
