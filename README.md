# Talaiot

[ ![Download](https://api.bintray.com/packages/cdsap/maven/talaiot/images/download.svg?version=0.1.8.1) ](https://bintray.com/cdsap/maven/talaiot/0.1.8.1/link)
 
Talaiot is a simple and extensible plugin to track timing/metrics in your Gradle tasks.  

_"... while some certainly had a defensive purpose, the use of others is not clearly understood. Some believe them to have served the purpose of lookout or signalling towers..."_

https://en.wikipedia.org/wiki/Talaiot


## Features Talaiot

* Focus on measuring within Time/Series systems
* Extensible definition of metrics depending of the requirements.
* Definition of custom publishers
* Develop it entirely with Kotlin 

## Setup Plugin

Include in the classpath the latest version of Talaiot:
````
classpath("com.cdsap:talaiot:latest_version")
````

Apply the plugin:

````
plugins {
    id("kotlin-android")
    id("talaiot")
}
````


## Basic configuration


````
talaiot {
    publishers {
        outputPublisher
    }
}
````


Simple task like `clean` will generate the output:


````
¯\_(ツ)_/¯ ¯\_(ツ)_/¯ ¯\_(ツ)_/¯ ¯\_(ツ)_/¯ ¯\_(ツ)_/¯ :app:clean ---- 51 ms
````

### More advanced configuration

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
Here we are adding the InfluxDb Publisher information to be reported and in terms of additional information tracked wea are removing the 
information related with Git and Performance

## DSL
### Talaiot Extension

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
    
### Publishers
In terms of publishing Talaiot inclide some default Publishers, but at the same time 
you can extend it and create your own publisher for your requirements

#### OutputPublisher
Simple output of the execution of the task. In console, at the end of the build will print by time each task  


| Property  |      Description                      |
|---------- |---------------------------------------|
| disabled  |  Disable the output of the execution  |



#### InfluxDbPublisher
One of the most populars Time Series Db. Talaiot will send to the server defined in the configuration the values collected during the execution


| Property  |      Description                         |
|---------- |------------------------------------------|
| dbName    | Name of the database                     |
| url       | Url of the InfluxDb Server               |
| urlMetric | Name of the metric used in the execution |

#### CustomPublisher
We may have different configurations or different services, Talaiot allows you to setup your favorite environment inside 
customPublisher configuration. 


| Property           |      Description                   |
|------------------- |------------------------------------|
| customPublisher    |  Custom Publisher ( see example)   |


### Metrics
With the metrics configuration we can adapt our requirements to the data we are adding on the information for 
every task.
The Default Configuration of Metrics includes:

Basic Metrics + gitMetrics + Performance metrics

But is possible that this solution doesn't solve your problem, and we offer the next configuration

| Property               |      Description                               |
|----------------------- |------------------------------------------------|
| gitMetrics             |Enable/Disable Git Metrics                             |
| performanceMetrics     |Enable/Disable Performance Metrics                     |
| customMetrics          |Confioguration to add new metrics (see example) |
| gradleMetrics          |Enable/Disable Gradle Metrics |

#### BaseMetrics
|  Values               |
|----------------------- |
| user             |
| project     |
| buildId          |
| os          |



#### PerformanceMetrics
Extracted from   Gradle Root's project configuration  and runtime Environment

|  Values               |
|----------------------- |
| totalMemory             |
| freeMemory     |
| maxMemory          |
| Xmx          |
| MaxPermSize          |


#### GitMetrics
|  Values               |  
|----------------------- | 
| gitUser             |
| branch     |         

#### GradleMetrics       
Extracted from Gradle Root's project configuration
        
|  Values               |     
|----------------------- |    
| gradleCaching             |       
| gradleDaemon     | 
| gradleParallel     | 
| gradleConfigurationOnDemand     | 
| gradleVersion     | 
               
 
               
## Creating custom Publishers
The configuration of Dashboards, TimeSeries DB's is different for peojects or companies. 
Talaiot allows you to create your custom Publisher:

1- The interface Publisher is the basic contract to publish the results of the build

````
interface Publisher {
    fun publish(measurementAggregated: TaskMeasurementAggregated)
}
````
     
The function publish includes the argument `TaskMeasurementAggregated` where includes all the aggregated metrics 
defined in the configuration and the list of tasks executed in the build. 

2- Create the Custom Publisher

````
class CustomPublisher : Publisher {

    override fun publish(measurementAggregated: TaskMeasurementAggregated) {

    }
}
````


3- Register the Custom Publisher      
The final step is just set the custom publisher in the `publisher` configuration

````
talaiot {
    publishers {
        customPublisher = CustomPublisher()
    }
}
````

4- In case you need to requires Http request to publish your results in third part services, Talaiot includes SimpleRequest 
and AutorizherRequest in case you need to use it. 

## Extending Metrics
If you need to add more information on the builds you can add more metrics under the `customMetrics` on the `MetricsConfiguration`

````
talaiot {
    metrics {
        customMetrics( "nameProject" to project.gradle.rootProject.name,
                             "customProperty" to getCustomProperty() )
    }
}
````

## Ignoring Executions
The configuration `ignoreWhen` allows us to set specific variables and values to ignore the execution of Talaiot when we match 
that condition.
One of the use cases is ignoring Talaiot on CI Executions becuasis understand better the performance of the builds on the development team, nor in CI. 


````
talaiot {
    ignoreWhen {
        envName = "CI"
        envValue = "true"
    }
}
````

## Creating DashBoards & Grafana
If your company/team don't use any Dashboard or just you want to test the whole process with Talaiot you can use Grafana and 
Influx Db 



## Thanks
Pascal Hartig, [Build Time Tracker](https://github.com/passy/build-time-tracker-plugin) it was totally an inspiration.
Anton Malinsky 
