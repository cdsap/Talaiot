# Talaiot

[ ![Download](https://api.bintray.com/packages/cdsap/maven/talaiot/images/download.svg?version=0.1.9) ](https://bintray.com/cdsap/maven/talaiot/0.1.9/link)


Talaiot is a simple and extensible plugin to track timing/metrics in your Gradle tasks.  

_"... while some certainly had a defensive purpose, the use of others is not clearly understood. Some believe them to have served the purpose of lookout or signalling towers..."_

https://en.wikipedia.org/wiki/Talaiot


## Features Talaiot

Talaiot is a complementary tool for medium/big teams of developers using Gradle Build System.

* Integration within Time/Series systems
* Extensible definition of metrics depending of the requirements.
* Definition of custom publishers
* Develop it entirely with Kotlin 

If you are wondering  Why another plugin to track the build,  Check the article explaining more about motivation of Talaiot

## Setup Plugin

Include in the classpath the latest version of Talaiot:
````
classpath("com.cdsap:talaiot:<latest_version>")
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
| customMetrics          |Configuration to add new metrics (see example) |
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
 
               
## Creating custom Publishers
The configuration of Dashboards, TimeSeries DB's is different for peojects or companies. 
Talaiot allows you to create your custom Publisher dependenciung of your requirements. 

 The interface Publisher is the basic contract to publish the results of the build

````
interface Publisher {
    fun publish(measurementAggregated: TaskMeasurementAggregated)
}
````
     
The function publish includes the argument `TaskMeasurementAggregated` where includes all the aggregated metrics 
defined in the configuration and the list of tasks executed in the build. 

If we want to create or custom Publisher first of all we have to create our class implementing the `Publisher`:

````
class CustomPublisher : Publisher {

    override fun publish(measurementAggregated: TaskMeasurementAggregated) {

    }
}
````
Therefore we need to implement our logic, could be reporting to another Time/Series environment like Prometheus or generate 
our custom Json implementation to later send it. 


Finally we need to register the Custom Publisher in the `talaiot` configuration:

````
talaiot {
    publishers {
        customPublisher = CustomPublisher()
    }
}
````
Check the wiki to see different examples of custom publishers defined in Java/Groovy.


## Creating DashBoards & Grafana
One of the moitivations of `Talaiot` is help to understand better our build process. One of the way to do that is measuring and 
having an easy way to display the information. 
One of the defauult publishers defined in Talaiot is `InfluxDbPublisher`, InfluxDb is quite extended in the "Dashboard workd".
The tupla Grafana/InfluxDb helps a lot of developers to track measurmentes happening oinf the db. 


### Docker Image
The docker image is provided there and is based in this awesome Image with some modfications, the docker iamage is 
usiong:

-- InfluxDb

-- Grafana

-- Provisioned Dashboard 


### Provisioning Data 
If you want to check quickly how Talaiot help us we need to populate the data. We can do with the script provisioning 
There we are using Gradle Profiler  




If your company/team don't use any Dashboard or just you want to test the whole process with Talaiot you can use Grafana and 
Influx Db 

In the article we were commenting some use cases used in Agoda

## Thanks
Pascal Hartig, [Build Time Tracker](https://github.com/passy/build-time-tracker-plugin) it was totally an inspiration.

Anton Malinsky for all the help. 
