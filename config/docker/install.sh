#!/bin/bash

echo 'DAEMON_OPTS="-a :80 -T localhost:6082 -f /etc/varnish/default.vcl -S /etc/varnish/secret -s malloc,256m"' >> /etc/default/varnish

sudo cp /app/config/docker/default.vlc /etc/varnish/default.vcl

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

# build and package app
sudo ./gradlew clean build
