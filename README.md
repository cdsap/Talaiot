# Talaiot

[ ![Download](https://api.bintray.com/packages/cdsap/maven/talaiot/images/download.svg?version=0.1.10) ](https://bintray.com/cdsap/maven/talaiot/0.1.10/link)


Talaiot is a simple and extensible plugin targeting teams using Gradle Build System.
It records duration of your Gradle tasks helping to understand problems of the build.  

Some of the features are:

* Integration with Time/Series systems 
* Extensible definition of metrics depending of the requirements.
* Definition of custom publishers
* Develop it entirely with Kotlin 

![](resources/dashboard.png) 


**_What is a Talaiot?_**

_"... while some certainly had a defensive purpose, the use of others is not clearly understood. Some believe them to have served the purpose of lookout or signalling towers..."_

https://en.wikipedia.org/wiki/Talaiot


## Setup Plugin

Include in the classpath the latest version of Talaiot:
````
classpath("com.cdsap:talaiot:<latest_version>")
````

Apply the plugin:

````
plugins {
    id("talaiot")
}
````

Check [this](https://github.com/cdsap/Talaiot/wiki/Groovy-setup) article to see how to setup Talaiot with  Groovy(all the examples in the README are in KTS.

## Basic configuration

````
talaiot {
    publishers {
        influxDbPublisher {
            dbName = "tracking"
            url = "http://localhost:3003"
            urlMetric = "tracking"
        }

    }
    metrics {
        gitMetrics = false
        performanceMetrics = false
    }
}
````
This example adds the `InfluxDbPublisher` with the information of the InfluxDb Server where it will be posted the information tracked.
Additionally we are disabling the metrics for Git and Performance.

## Talaiot Extension

| Property  |      Description                                                       |
|---------- |------------------------------------------------------------------------|
| logger    | Mode for logging (Silent,Info)                                         |
| ignoreWhen| Configuration to ignore the execution of Talaiot                       |
| publishers| Configuration to define where to submit the information of the build   |
| metrics   | Additional information tracked during the execution of the task        |

### IgnoreWhen

| Property   |      Description      |
|----------- |-----------------------|
| envName    |Name of the Property   |
| envValue   |Value of the Property  |

We will use IgnoreWhebn when we want to ignore publishing the results of the build. One use case is to ignore it when we 
are building on CI 

````
talaiot {
    ignoreWhen {
        envName = "CI"
        envValue = "true"
    }
}
````


    
### Publishers
In terms of publishing Talaiot include some default Publishers, but at the same time 
you can extend it and create your own publisher for your requirements

#### Predefined Publishers
Simple output of the execution of the task. In console, at the end of the build will print by time each task  


| Property           |      Description                                                                                       |
|------------------- |--------------------------------------------------------------------------------------------------------|
| OutputPublisher    | Publish the results of the build on console, this Publisher will only print the task name and duration |
| InfluxDbPublisher  | Publish the results of the build to the InfluxDb database defined in the configuration                |



#### InfluxDbPublisher
Talaiot will send to the InfluxDb server defined in the configuration the values collected during the execution


| Property  |      Description                         |
|---------- |------------------------------------------|
| dbName    | Name of the database                     |
| url       | Url of the InfluxDb Server               |
| urlMetric | Name of the metric used in the execution |


#### Custom Publishers
Talaiot allows to use another Publishers defined by the requirements of your environment, in case you are using another implementation.
Check [here](https://github.com/cdsap/Talaiot/wiki/Publishers#custompublisher) how to define a custom publisher

### Metrics
For every measurement done, Talaiot adds metrics to help you later to analyze the data and detect problems.

Metrics are categorized by different configurations.

The Default Configuration of Metrics includes:

| Property               |      Description                                                     |
|----------------------- |----------------------------------------------------------------------|
| baseMetrics            |Collects information about the project, build, OS Id and user         |
| gitMetrics             |Metrics related to the Git configuration of the project               |
| performanceMetrics     |Metrics related to the Java arguments defined on the Gradle Build.    |
| gradleMetrics          |Metrics related to Gradle arguments                                   |

By default all the metrics are available but if you want to disable some group define the configuration like:
```
  metrics {
        gitMetrics = false
        perfomanceMetrics = false
  }
```


Check the [Wiki](https://github.com/cdsap/Talaiot/wiki/Metrics) to know more about the existing metrics

#### Extending metrics


If you need to add more information on the builds you can add more metrics under the `customMetrics` on the `MetricsConfiguration`

````
talaiot {
    metrics {
        customMetrics( "versionApp" to $version,
                       "customProperty" to getCustomProperty() 
                      )
    }
}
````
 
               
## Example: Analyzing Data provided by Talaiot

### Docker, InfluxDb and Grafana
To have a quick setup to see the possibilities of `Talaiot` we are providing a Docker image to set up quickly a Grafana + Inlfluxdb instances(based on [this](https://github.com/philhawthorne/docker-influxdb-grafana) great repo).  

Additionally we create a default database and a provisioned dashboard.
The source is [here](docker/Dockerfile):

TO run the Docker Image:
```sh
docker run -d \
  -p 3003:3003 \
  -p 3004:8083 \
  -p 8086:8086 \
  -p 22022:22 \
  -v /var/lib/influxdb \
  -v /var/lib/grafana \
  cdsap/talaiot:latest
```
  
And you can access to the local instance of Grafana through:

`http://localhost:3003` root/root
    
Finally, we are providing database and datasource for the InfluxDb instance to quick setup your configuration.


### Populating data 
If you access to the provisioned Dashbord included in the Docker Image(http://localhost:3003/d/F9jppxQiz/android-task-tracking?orgId=1) you will see an empty dashboard like:

![](resources/empty_dashboard.png)

If you want to check quickly how Talaiot help us you need to populate the data. We are providing script to populate data based in this 
example repository:
https://github.com/cdsap/TalaiotClientExample

This repository includes the `InfluxDbPubluser` configuration pointing to the InfluxDb and datastore defined in the Docker image:

```
talaiot {
    publishers {
        influxDbPublisher {
            dbName = "tracking"
            url = "http://localhost:3003"
            urlMetric = "tracking"
        }

    }
}

```

You can execute the script:

`bash populateDashboard/populate.sh `

The script will download the example repository and with the help of Gradle Profiler(https://github.com/gradle/gradle-profiler) 
will trigger number of builds defined in the scenario file:

```
    assemble {
    tasks = ["clean"]
    }
    clean_build {
    versions = ["5.1","4.10.2"]
    tasks = ["assembleDebug"]
    gradle-args = ["--parallel"]
    cleanup-tasks = ["clean"]
    run-using = cli
    warm-ups = 20
    }
```

Once is finished you can check the results on the Grafana Dashboard http://localhost:3003/d/F9jppxQiz/android-task-tracking?orgId=1


## Other Plugins
Talaiot is not a new idea, there are multiple awesome plugins to use to achieve the same results:

* [Gradle Enterprise](https://gradle.com/#): Actually if you are using Gradle Enterprise Talaiot is useless because the aggregation 
is great and you have the support from Gradle :) 

* [Build Time Tracker](https://github.com/passy/build-time-tracker-plugin) by Pascal Hartig(@passy).

* [Kuronometer](https://github.com/pedrovgs/Kuronometer): Plugin developed with Scala and FP concepts by Pedro Vicente Gómez Sánchez(@pedrovgs)


## Contributing
Talaiot is Open Source and accepts contributions of new Publishers, Metrics and Dashboards that we can include as provisioned ones in the Docker image.

## Thanks
Pascal Hartig, [Build Time Tracker](https://github.com/passy/build-time-tracker-plugin) it was totally an inspiration to build this plugin.

