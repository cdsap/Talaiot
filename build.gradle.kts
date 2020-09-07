
buildscript {
    val kotlin_version by extra("1.3.72")
    repositories {
        jcenter()
    }
    dependencies {
        "classpath"("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}