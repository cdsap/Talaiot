#!/bin/sh
#############
##
##  $1: Path of execution
##  $2: Gradle Profiler
##  $3: Repository Project
##  $4: scenario file
##
##
###############
path=$1
gradle_profiler=$2
repository_project=$3
scenario_file=$4


# calculating path
if [ "$path" == "" ]; then
   path="./"
fi

cd $path

# Cloning Repo Gradle Profiler

if [ "$gradle_profiler" == ""]; then
  git clone https://github.com/gradle/gradle-profiler.git
  cd gradle-profiler

  # Installing Gradle Profiler binary
  ./gradlew installDist

  # creating env variable for Gradle Profiler
  export PATH=$PATH:build/install/gradle-profiler/bin
  gradle_profiler= gradle-profiler
  cd ..
fi

if ["$repository_project" == ""]; then
  # cloning test project
  git clone https://github.com/cdsap/TalaiotClientExample.git
  repository_project=./TalaiotClientExample
fi

# executing Gradle Profiler with Test code

$gradle_profiler --benchmark --project-dir $repository_project --scenario-file  bootstraping/scenario
