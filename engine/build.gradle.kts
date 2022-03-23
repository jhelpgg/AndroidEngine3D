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
    testImplementation(Constants.junitLibrary)
    androidTestImplementation(Constants.androidxTestLibrary)
    androidTestImplementation(Constants.espressoLibrary)

    implementation(project(path = ":utilities"))
    implementation(project(path = ":lists"))
    implementation(project(path = ":tasks"))
    implementation(project(path = ":io"))
    implementation(project(path = ":images"))
    implementation(project(path = ":sound"))
    implementation(project(path = ":animations"))
}
