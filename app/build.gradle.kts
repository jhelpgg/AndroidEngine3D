/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can"t do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can"t change that fact.
 */

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("org.jetbrains.dokka")
}

android {
    compileSdk = Constants.compileSdkVersion

    defaultConfig {
        minSdk = Constants.minSdkVersion
        targetSdk = Constants.targetSdkVersion
        applicationId = "fr.jhelp.multitools"
        versionCode = Constants.jhelpAndroidSDKversionCode
        versionName = Constants.jhelpAndroidSDKversion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packagingOptions {
        resources.excludes.add("META-INF/LICENSE*")
        resources.excludes.add("META-INF/NOTICE*")
    }
}

dependencies {
    implementation(Constants.kotlinLibrary)
    implementation(Constants.appcompatLibrary)
    implementation(Constants.androidxCoreLibrary)
    implementation(Constants.constraintlayoutLibrary)
    implementation(Constants.lifecycleExtensionsLibrary)
    implementation(Constants.lifecycleViewmodelLibrary)
    testImplementation(Constants.junitLibrary)
    testRuntimeOnly(Constants.junitLibraryEngine)
    androidTestImplementation(Constants.androidxTestLibrary)
    androidTestImplementation(Constants.espressoLibrary)
    implementation(Constants.androidxRecyclerviewLibrary)

    implementation(project(path = ":utilities"))
    implementation(project(path = ":tasks"))
    implementation(project(path = ":lists"))
    implementation(project(path = ":io"))
    implementation(project(path = ":security"))
    implementation(project(path = ":engine"))
    implementation(project(path = ":images"))
    implementation(project(path = ":sound"))
    implementation(project(path = ":database"))
    implementation(project(path = ":graphics"))
    implementation(project(path = ":animations"))
    implementation(project(path = ":models"))
    implementation(project(path = ":provided"))
}

tasks.named<org.jetbrains.dokka.gradle.DokkaTask>("dokkaHtml").configure {
    outputDirectory.set(File(buildDir.parentFile,"src/doc/dokka"))
}
