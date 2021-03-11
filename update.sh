#!/usr/bin/env bash

git pull
./gradlew clean build -Pvaadin.productionMode --info --no-daemon

docker-compose down
docker-compose up -d
