#!/bin/bash

# 1. Check for the .env file
if [ ! -f .env ]; then
    echo "❌ Error: .env file missing. Cannot pass variables."
    exit 1
fi

# 2. Extract values and pass them as -D arguments
# This 'pipes' them directly into the Maven execution
mvn spring-boot:run -pl infrastructure \
  -Dspring-boot.run.jvmArguments="\
    -DDB_NAME=$(grep DB_NAME .env | cut -d '=' -f2) \
    -DDB_USERNAME=$(grep DB_USERNAME .env | cut -d '=' -f2) \
    -DDB_PASSWORD=$(grep DB_PASSWORD .env | cut -d '=' -f2) \
    -DDB_HOST=$(grep DB_HOST .env | cut -d '=' -f2) \
    -DDB_PORT=$(grep DB_PORT .env | cut -d '=' -f2)"