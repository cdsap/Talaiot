# Docker Image with InfluxDB and Grafana

This is a Docker image based on the awesome [Docker Image with Telegraf (StatsD), InfluxDB and Grafana](https://github.com/samuelebistoletti/docker-statsd-influxdb-grafana) from [Samuele Bistoletti](https://github.com/samuelebistoletti).

The main point of difference with this image is:

* Persistence is supported via mounting volumes to a Docker container
* Grafana will store its data in SQLite files instead of a MySQL table on the container, so MySQL is not installed
* Telegraf (StatsD) is not included in this container

The main purpose of this image is to be used to show data from a [Home Assistant](https://home-assistant.io) installation. For more information on how to do that, please see my website about how I use this container.

| Description  | Value |
|--------------|-------|
| Docker Image | 2.0.0 |
| Ubuntu       | 16.04 |
| InfluxDB     | 1.2   |
| Grafana      | 4.1.1 |

## Quick Start

To start the container with persistence you can use the following:

```sh
docker run -d \
  --name docker-influxdb-grafana \
  -p 3003:3003 \
  -p 3004:8083 \
  -p 8086:8086 \
  -p 22022:22 \
  -v /path/for/influxdb:/var/lib/influxdb \
  -v /path/for/grafana:/var/lib/grafana \
  philhawthorne/docker-influxdb-grafana:latest
```

To stop the container launch:

```sh
docker stop docker-influxdb-grafana
```

To start the container again launch:

```sh
docker start docker-influxdb-grafana
```

## Mapped Ports

```
Host		Container		Service

3003		3003			grafana
3004		8083			influxdb-admin
8086		8086			influxdb
22022		22				sshd
```
## SSH

```sh
ssh root@localhost -p 22022
```
Password: root

## Grafana

Open <http://localhost:3003>

```
Username: root
Password: root
```

### Add data source on Grafana

1. Using the wizard click on `Add data source`
2. Choose a `name` for the source and flag it as `Default`
3. Choose `InfluxDB` as `type`
4. Choose `direct` as `access`
5. Fill remaining fields as follows and click on `Add` without altering other fields

Basic auth and credentials must be left unflagged. Proxy is not required.

Now you are ready to add your first dashboard and launch some queries on a database.

## InfluxDB

### Web Interface

Open <http://localhost:3004>

```
Username: root
Password: root
Port: 8086
```

### InfluxDB Shell (CLI)

1. Establish a ssh connection with the container
2. Launch `influx` to open InfluxDB Shell (CLI)
