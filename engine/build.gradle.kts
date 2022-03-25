plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.jetbrains.dokka")
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
}

dependencies {
    implementation(Constants.kotlinLibrary)
    implementation(Constants.androidxCoreLibrary)
    implementation(Constants.appcompatLibrary)
    testImplementation(Constants.junitLibrary)
    testRuntimeOnly(Constants.junitLibraryEngine)
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

tasks.named<org.jetbrains.dokka.gradle.DokkaTask>("dokkaHtml").configure {
    outputDirectory.set(File(buildDir.parentFile,"src/doc/dokka"))
}
