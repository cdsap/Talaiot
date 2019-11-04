#!/bin/sh

./gradlew publishMavenPublicationToLocalRepository
mkdir tmp
cd tmp
## Cloning Repo Gradle Profiler
git clone https://github.com/gradle/gradle-profiler.git
cd gradle-profiler
## Installing Gradle Profiler binary
cd gradle-profiler
./gradlew installDist
## creating env variable for Gradle Profiler
export PATH=$PATH:build/install/gradle-profiler/bin
# executing Gradle Profiler with Test code
gradle-profiler --benchmark --project-dir ../../sample/ --scenario-file  ../../scripts/scenario
