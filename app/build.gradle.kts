@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    id("com.google.devtools.ksp") version "1.9.0-1.0.11"
}

android {
    namespace = "com.example.exchanger"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.exchanger"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":common:transfer"))
    implementation(project(":common:core"))

    //Core
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.androidx.appcompat)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ui.test.junit4)
    androidTestImplementation(libs.androidx.test.ext.junit)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.compose.bom))

    //UI
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.flexbox)

    //DI
    implementation(libs.koin.android)
    implementation(libs.koin.android.compat)
    implementation(libs.koin.core)

    //Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.dynamic.features.fragment)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //Network
    implementation(libs.retrofit)
    implementation(libs.retrofitMock)
    implementation(libs.retrofitMoshi)
    ksp(libs.moshi.kotlin.codegen)
    implementation(libs.moshi.kotlin)
    implementation(libs.moshi.adapters)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
}