#!/bin/bash
sudo su -
apt-get update
apt-get install -y varnish activemq postgresql openjdk-7-jdk nodejs dos2unix
ln -f -s /vagrant /app
chmod +x /app/config/docker/install.sh
chmod +x /app/config/docker/start.sh
dos2unix /app/config/docker/install.sh
dos2unix /app/config/docker/start.sh
dos2unix /app/config/upstart/wellington.conf
dos2unix /app/gradlew
/app/config/docker/install.sh
cp /app/config/upstart/wellington.conf /etc/init/wellington.conf
start wellington
