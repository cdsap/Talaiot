#!/bin/sh

git clone https://github.com/gradle/gradle-profiler.git

cd gradle-profiler

./gradlew installDist

export PATH=$PATH:build/install/gradle-profiler/bin
cd ..

git clone https://github.com/cdsap/TalaiotClientExample.git



gradle-profiler --benchmark --project-dir ./TalaiotClientExample --scenario-file  bootstraping/scenario


