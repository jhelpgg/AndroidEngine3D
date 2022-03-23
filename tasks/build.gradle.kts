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
}

dependencies {
    implementation(Constants.kotlinLibrary)
    implementation(Constants.androidxCoreLibrary)
    implementation(Constants.appcompatLibrary)
    implementation(Constants.lifecycleViewmodelLibrary)
    implementation(Constants.lifecycleExtensionsLibrary)
    testImplementation(Constants.junitLibrary)
    androidTestImplementation(Constants.androidxTestLibrary)
    androidTestImplementation(Constants.espressoLibrary)

    implementation(project(path = ":utilities"))
    implementation(project(path = ":lists"))
}
