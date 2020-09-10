#!/bin/bash

./gradlew  vaadinClean vaadinPrepareFrontend
rm -rf webpack*
/usr/bin/npm --no-update-notifier --no-audit install
./gradlew vaadinBuildFrontend
