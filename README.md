# Talaiot

[ ![Download](https://api.bintray.com/packages/cdsap/maven/talaiot/images/download.svg?version=0.1.9) ](https://bintray.com/cdsap/maven/talaiot/0.1.9/link)


Talaiot is a simple and extensible plugin to track length your Gradle tasks and add metrics in  .  
 

Talaiot is targeting medium/big teams of developers using Gradle Build System.
Some of the features are:

* Integration within Time/Series systems
* Extensible definition of metrics depending of the requirements.
* Definition of custom publishers
* Develop it entirely with Kotlin 

If you are wondering  Why we need another plugin to track builds, check this article that explains more about motivation of Talaiot


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

Check these articles to see how to setup with Groovy(all the examples in the README are in KTS.
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
Here we are adding the `InfluxDbPublisher` information to be reported and in terms of additional information tracked wea are removing the 
information related with Git and Performance

## Talaiot Extension

| Property  |      Description                                   |
|---------- |----------------------------------------------------|
| logger    | State for logging, true by default                 |
| ignoreWhen| Configuration to ignore the execution of Talaiot   |
| publishers| Once the build has finished                        |
| metrics   | Values tracked in the execution of the task        |

### IgnoreWhen

| Property   |      Description      |
|----------- |-----------------------|
| envName    |Name of the Property   |
| envValue   |Value of the Property  |

We will use IgnoreWhebn when we want to ignore publishing the results of the build due 

````
talaiot {
    ignoreWhen {
        envName = "CI"
        envValue = "true"
    }
}
````


    
### Publishers
In terms of publishing Talaiot inclide some default Publishers, but at the same time 
you can extend it and create your own publisher for your requirements

#### Defined Publsihers
Simple output of the execution of the task. In console, at the end of the build will print by time each task  


| Property           |      Description                      |
|------------------- |---------------------------------------|
| OutputPublisher    |  Disable the output of the execution  |
| InfluxDbPublisher  |  Disable the output of the execution  |
| CustomPublisher    |  Disable the output of the execution  |



#### InfluxDbPublisher
One of the most populars Time Series Db. Talaiot will send to the server defined in the configuration the values collected during the execution


| Property  |      Description                         |
|---------- |------------------------------------------|
| dbName    | Name of the database                     |
| url       | Url of the InfluxDb Server               |
| urlMetric | Name of the metric used in the execution |

#### Custom Publishers
Check here how to define a custom publisher


### Metrics
With the metrics configuration we can adapt our requirements to the data we are adding on the information for 
every task.
The Default Configuration of Metrics includes:

Basic Metrics + gitMetrics + Performance metrics

But is possible that this solution doesn't solve your problem, and we offer the next configuration

| Property               |      Description                               |
|----------------------- |------------------------------------------------|
| baseMetricsg             |Enable/Disable Git Metrics                      |
| gitMetrics             |Enable/Disable Git Metrics                      |
| performanceMetrics     |Enable/Disable Performance Metrics              |
| customMetrics          |Configuration to add new metrics (see example)  |
| gradleMetrics          |Enable/Disable Gradle Metrics |

Check the Wiki to know more about the existing metrics
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
 
               
##Analyzing Data provided by Talaiot

### Docker, InfluxDb and Grafana
To have a quick setup to see the posibilities of `Talaiot` we are providing a Docker image to set up quickly a Grafana + Inlfluxdb 
instances. 
Additionally we set up a default database and a provisioned dashboard.
The source is here(docker folder):
But if you want to set up easyly only 

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
    
In terms of InfluxdB we are providing one database and one dashboard plus the datasource    
### Populating data 
If you access to the provisioned Dashbord included in the Docker Image(http://localhost:3003/d/F9jppxQiz/android-task-tracking?orgId=1)
 you will see an empty dashboard like:

![](resources/empty_dashboard.png)

If you want to check quickly how Talaiot help us we need to populate the data. We can do with the script provisioning 
There we are using Gradle Profiler .
Gradle Profiler option if Benchmarking helps us to execute different scenarios. To populate quickly we are exect

only execute:

`bash boolstraping/populate.sh --YOUR_PATH` 
this will doenload the gradle profiler will add to your path and execute the scenatio. 

This is an example of scenario defined in the code:

```

    aessemble {
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

To check 

 As you can see we don't have data to analyz. 
 We are providing an example of 


## Thanks
Pascal Hartig, [Build Time Tracker](https://github.com/passy/build-time-tracker-plugin) it was totally an inspiration.

Anton Malinsky for all the help. 
