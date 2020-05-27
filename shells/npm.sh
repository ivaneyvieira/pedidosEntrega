#!/bin/bash

./gradlew  npmSetup npmInstall
 /usr/bin/npm --no-update-notifier --no-audit install
./gradlew vaadinPrepareNode