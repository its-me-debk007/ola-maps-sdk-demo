import java.util.Properties

plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
//    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.ksp)
//    alias(libs.plugins.compose.compiler)
    id("kotlin-kapt")
}

android {
    namespace = "com.debk007.olamaps"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.debk007.olamaps"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")

        if (localPropertiesFile.exists()) {
            localProperties.load(localPropertiesFile.inputStream())

            val clientId = localProperties.getProperty("CLIENT_ID")
            val clientSecret = localProperties.getProperty("CLIENT_SECRET")
            val apiKey = localProperties.getProperty("API_KEY")

            buildConfigField("String", "CLIENT_ID", "\"${clientId}\"")
            buildConfigField("String", "CLIENT_SECRET", "\"${clientSecret}\"")
            buildConfigField("String", "API_KEY", "\"${apiKey}\"")
        }

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
//        compose = true
        viewBinding = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
//    implementation(libs.activity.compose)
//    implementation(platform(libs.compose.bom))
//    implementation(libs.ui)
//    implementation(libs.ui.graphics)
//    implementation(libs.ui.tooling.preview)
//    implementation(libs.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
//    androidTestImplementation(platform(libs.compose.bom))
//    androidTestImplementation(libs.ui.test.junit4)
//    debugImplementation(libs.ui.tooling)
//    debugImplementation(libs.ui.test.manifest)
    implementation(libs.retrofit)
//    implementation(libs.moshi.converter)
//    ksp(libs.moshi.kotlin.codegen)
    implementation(libs.gson.converter)
//    implementation(libs.glide)
    implementation(libs.coroutines.android)
    implementation(libs.coroutines.core)
//    implementation(libs.hilt.android)
//    ksp(libs.hilt.kapt)
//    implementation(libs.hilt.navigation.compose)
//    implementation(libs.navigation.compose)
//    debugImplementation(libs.chucker.debug)
//    releaseImplementation(libs.chucker.release)

    implementation(libs.moe.android.sdk)
    implementation(libs.android.sdk)
    implementation(libs.android.sdk.directions.models)
    implementation(libs.android.sdk.services)
    implementation(libs.android.sdk.turf)
    implementation(libs.android.plugin.markerview.v9)
    implementation(libs.android.plugin.annotation.v9)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)
    implementation(libs.androidx.appcompat)
    implementation(libs.lifecycle.extensions)
    kapt(libs.lifecycle.compiler)
    implementation(files("libs/maps-1.0.68.aar"))
}
