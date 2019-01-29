#!/bin/sh

git clone https://github.com/gradle/gradle-profiler.git

cd gradle-profiler

./gradlew installDist

export PATH=$PATH:build/install/gradle-profiler/bin
cd ..

gradle-profiler --benchmark --project-dir . --scenario-file  bootstraping/scenario


