#!/usr/bin/env bash

echo "Running static analysis..."

# Validate Kotlin code with detekt
./gradlew detektAll --stacktrace --no-daemon

status=$?

if [ "$status" = 0 ] ; then
    echo "Static analysis found no issues. Proceeding with commit."
    exit 0
else
    echo 1>&2 "Static analysis found issues you need to fix before committing."
    exit 1
fi
