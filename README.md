## Talaiot[WIP]

Talaiot is a simple and extensible plugin to track timing in your Gradle Project.  

_"... while some certainly had a defensive purpose, the use of others is not clearly understood. Some believe them to have served the purpose of lookout or signalling towers..."_


#### Why another plugin?

Maybe you are wondering why we need another tool to track time tasks.
You have already awesome plugins like https://github.com/passy/build-time-tracker-plugin or of course Build Scan from Gradle.
 
Build scans are excellent tools to understand problems on the build, detect bottlenecks and problems on the configuration, but if 
you need to aggregate data you need to join Gradle Enterprise. 
 
 In our case we didn't have the subscription of Gradle enterprise we wanted to understand what is the best approach the data
 and understand the problems of medium/big teams in terms of detection and developemnt. 


#### DSL
##### TalaiotExtension

| Property  |      Are      |
|---------- |:-------------:|
| logger    |  left-aligned |
| ignoreWhen|    centered   |
| publishers| right-aligned |
| metrics   | right-aligned |

##### IgnoreWhen

| Property  |      Are      |
|---------- |:-------------:|
| envName    |  left-aligned |
| envValue   |    centered   |


##### Publishers

| Property  |      Are      |
|---------- |:-------------:|
| envName    |  left-aligned |
| envValue   |    centered   |


##### Publishers

| Property  |      Are      |
|---------- |:-------------:|
| envName    |  left-aligned |
| envValue   |    centered   |



#### Basic configuration


#### Custom Publishers


#### Extending Metrics

#### Creating DashBoards & Grafana


#### Undestanting problems



#### Thanks