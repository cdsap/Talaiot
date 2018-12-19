buildscript {
    repositories {
        google()
        jcenter()
        mavenLocal()
        maven(url = "https://dl.bintray.com/kotlin/ktor")
    }
    
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.11")
        classpath ("com.novoda:bintray-release:0.9")

    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenLocal()
    }
}

tasks {

    val plugin by registering(GradleBuild::class) {
        dir = file("talaiot")
        tasks = listOf("publish")
    }
    
    consumer {
        dependsOn(plugin)
    }
}