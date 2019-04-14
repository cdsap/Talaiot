FROM ubuntu:16.04
MAINTAINER Inaki

ENV DEBIAN_FRONTEND noninteractive
ENV LANG C.UTF-8

# Default versions
ENV INFLUXDB_VERSION 1.2.0
ENV GRAFANA_VERSION  5.4.3

# Database Defaults
ENV INFLUXDB_GRAFANA_DB datasource
ENV INFLUXDB_GRAFANA_USER datasource
ENV INFLUXDB_GRAFANA_PW datasource

ENV GF_DATABASE_TYPE=sqlite3


# Clear previous sources
RUN rm /var/lib/apt/lists/* -vf

# Base dependencies
RUN apt-get -y update && \
 apt-get -y dist-upgrade && \
 apt-get -y --force-yes install \
  apt-utils \
  ca-certificates \
  curl \
  git \
  htop \
  libfontconfig \
  nano \
  net-tools \
  openssh-server \
  supervisor \
  wget && \
 curl -sL https://deb.nodesource.com/setup_7.x | bash - && \
 apt-get install -y nodejs

WORKDIR /root

RUN mkdir -p /var/log/supervisor && \
    mkdir -p /var/run/sshd && \
    sed -i 's/PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config && \
    echo 'root:root' | chpasswd && \
    rm -rf .ssh && \
    rm -rf .profile && \
    mkdir .ssh

# Install InfluxDB
RUN wget https://dl.influxdata.com/influxdb/releases/influxdb_${INFLUXDB_VERSION}_amd64.deb && \
    dpkg -i influxdb_${INFLUXDB_VERSION}_amd64.deb && rm influxdb_${INFLUXDB_VERSION}_amd64.deb

# Install Grafana
RUN wget https://dl.grafana.com/oss/release/grafana_${GRAFANA_VERSION}_amd64.deb && \
    dpkg -i grafana_${GRAFANA_VERSION}_amd64.deb && rm grafana_${GRAFANA_VERSION}_amd64.deb

# Cleanup
RUN apt-get clean && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

# Configure Supervisord, SSH and base env
COPY supervisord/supervisord.conf /etc/supervisor/conf.d/supervisord.conf
COPY ssh/id_rsa .ssh/id_rsa
COPY bash/profile .profile

# Configure InfluxDB
COPY influxdb/influxdb.conf /etc/influxdb/influxdb.conf
COPY influxdb/init.sh /etc/init.d/influxdb

# Configure Grafana
COPY grafana/grafana.ini /etc/grafana/grafana.ini
RUN mkdir -p /usr/share/grafana/provisioning/dashboards/ && \
    mkdir -p /usr/share/grafana/provisioning/datasources/ && \
    mkdir -p /var/lib/grafana/dashboards

COPY grafana/conf/provisioning/dashboards /usr/share/grafana/provisioning/dashboards/
COPY grafana/conf/provisioning/datasources /usr/share/grafana/provisioning/datasources/
COPY grafana/dashboards /var/lib/grafana/dashboards/

RUN chmod 0755 /etc/init.d/influxdb


CMD ["/usr/bin/supervisord"]

COPY influxdb/createdb.sh /usr/local/bin/createdb.sh

# Create Database
RUN chmod 777 /usr/local/bin/createdb.sh

RUN grafana-cli plugins install grafana-piechart-panel
