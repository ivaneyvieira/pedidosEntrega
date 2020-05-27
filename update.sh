#!/usr/bin/env bash

git pull
./gradlew clean build

docker-compose down
docker-compose up -d
