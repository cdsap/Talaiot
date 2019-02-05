#!/bin/sh

mkdir tmp
cd tmp

# Cloning Repo Gradle Profiler

#git clone https://github.com/gradle/gradle-profiler.git
#cd gradle-profiler

# Installing Gradle Profiler binary
#./gradlew installDist

# creating env variable for Gradle Profiler
#export PATH=$PATH:build/install/gradle-profiler/bin
#cd ..

# cloning test project
#git clone https://github.com/cdsap/TalaiotClientExample.git

# executing Gradle Profiler with Test code
pwd
gradle-profiler --benchmark --project-dir TalaiotClientExample/ --scenario-file  ../populateDashboard/scenario
