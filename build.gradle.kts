// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.1.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.6.10")
    }
}

plugins {
    id("maven-publish")
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

