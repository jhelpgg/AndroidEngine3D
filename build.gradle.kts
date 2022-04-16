// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.1.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    }
}

plugins {
    id("maven-publish")
    id("org.jetbrains.kotlin.jvm") version "1.6.20" apply false
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }
}

task("clean").apply {
    delete(rootProject.buildDir)
}

