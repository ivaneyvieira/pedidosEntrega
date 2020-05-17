#!/usr/bin/env bash

git pull
gradle clean build

docker-compose down
docker-compose up -d
