#!/bin/bash

sudo service postgresql start

sudo -u postgres psql postgres -c "ALTER USER postgres WITH PASSWORD 'connect';"

sudo -u postgres createdb -O postgres spring

sudo service postgresql restart

sudo ln -s /etc/activemq/instances-available/main /etc/activemq/instances-enabled/main

sudo service activemq start

chmod 0755 /app/gradlew

cd /app

# run flyway to update DB
sudo ./gradlew flywayMigrate

# build the app
sudo ./gradlew build

# run spring boot app
sudo ./gradlew bootRun

/bin/bash