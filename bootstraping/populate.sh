#!/bin/sh

# calculating path
if [ "$1" == "" ]; then
   path="./"
else
   path=$1
fi

cd $path

# Cloning Repo Gradle Profiler
git clone https://github.com/gradle/gradle-profiler.git
cd gradle-profiler

# Installing Gradle Profiler binary
./gradlew installDist

# creating env variable for Gradle Profiler
export PATH=$PATH:build/install/gradle-profiler/bin

cd ..

# cloning test project
git clone https://github.com/cdsap/TalaiotClientExample.git

# executing Gradle Profiler with Test code
gradle-profiler --benchmark --project-dir ./TalaiotClientExample --scenario-file  bootstraping/scenario


