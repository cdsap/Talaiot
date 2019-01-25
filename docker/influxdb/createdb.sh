#!/bin/sh

curl -G 'http://localhost:8086/query' --data-urlencode "q=CREATE DATABASE tracking"
