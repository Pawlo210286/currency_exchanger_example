@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("java-library")
    alias(libs.plugins.org.jetbrains.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {

    implementation(project(":common:multifunctions"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.retrofit)
    implementation(libs.retrofit)
    implementation(libs.retrofitMock)
    implementation(libs.retrofitMoshi)
}