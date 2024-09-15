plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.brenn.firstapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.brenn.firstapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(files("C:/Users/trompe/Downloads/sdk-moncash-api-1.0.5-SNAPSHOT.jar"))
    implementation(libs.firebase.crashlytics.buildtools)
    implementation(files("C:/Users/trompe/Downloads/gson-2.8.5.jar"))
    implementation(libs.jakarta.ws.rs.api)
    implementation(files("C:/Users/trompe/Downloads/genson-1.6.jar"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}