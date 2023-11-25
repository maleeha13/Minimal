// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript{
    var kotlin_version = "1.9.0"

    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
//        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.30")
//        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
        // https://mvnrepository.com/artifact/com.android.tools.build/gradle
//        classpath("com.android.tools.build:gradle:8.1.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("com.android.tools.build:gradle:7.2.0")


    }
}

plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.6.0" apply false
    id("com.android.library") version "8.1.0" apply false
}