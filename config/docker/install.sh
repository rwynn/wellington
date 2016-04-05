#!/bin/bash

sudo cp /app/config/docker/varnish /etc/default/varnish
sudo cp /app/config/docker/default.vlc /etc/varnish/default.vcl

sudo service varnish restart

sudo service postgresql start

sudo -u postgres psql postgres -c "ALTER USER postgres WITH PASSWORD 'connect';"

sudo -u postgres createdb -O postgres spring

sudo service postgresql restart

sudo ln -s /etc/activemq/instances-available/main /etc/activemq/instances-enabled/main

sudo service activemq restart

chmod 0755 /app/gradlew

cd /app

# run flyway to update DB
sudo ./gradlew flywayMigrate

# build and package app
sudo ./gradlew clean build
