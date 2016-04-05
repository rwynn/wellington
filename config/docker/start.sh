#!/bin/bash

sudo service postgresql start

sudo service activemq start

sudo service varnish start

cd /app

# run spring boot app
sudo ./gradlew bootRun

/bin/bash
