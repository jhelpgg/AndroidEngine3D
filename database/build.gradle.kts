/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can"t do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can"t change that fact.
 */

plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdk = Constants.compileSdkVersion

    defaultConfig {
        minSdk = Constants.minSdkVersion
        targetSdk = Constants.targetSdkVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packagingOptions {
        resources.excludes.add("META-INF/LICENSE*")
        resources.excludes.add("META-INF/NOTICE*")
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(Constants.kotlinLibrary)
    implementation(Constants.androidxCoreLibrary)
    implementation(Constants.appcompatLibrary)
    implementation(Constants.kotlinReflect)
    testImplementation(Constants.junitLibrary)
    testRuntimeOnly(Constants.junitLibraryEngine)
    androidTestImplementation(Constants.androidxTestLibrary)
    androidTestImplementation(Constants.espressoLibrary)
    androidTestImplementation(Constants.junitLibrary)
    androidTestRuntimeOnly(Constants.junitLibraryEngine)

    implementation(project(path = ":tasks"))
    implementation(project(path = ":utilities"))
    implementation(project(path = ":io"))
    implementation(project(path = ":lists"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}

