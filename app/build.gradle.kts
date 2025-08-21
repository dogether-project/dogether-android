import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}
val kakaoNativeAppKey = run {
    val propsFile = rootProject.file("local.properties")
    if (propsFile.exists()) {
        val props = Properties().apply { load(propsFile.inputStream()) }
        props.getProperty("KAKAO_NATIVE_APP_KEY")?.trim() ?: ""
    } else ""
}

android {
    namespace = "site.dogether.android"
    compileSdk = 36

    defaultConfig {
        applicationId = "site.dogether.android"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"

        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "\"$kakaoNativeAppKey\"")
        manifestPlaceholders["KAKAO_NATIVE_APP_KEY"] = kakaoNativeAppKey

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(project(":presentation"))
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":common"))

    // Compose BOM
    implementation(platform(libs.androidx.compose.bom))

    // Compose core
    implementation(libs.bundles.compose.core)

    // Orbit
    implementation(libs.bundles.orbit)

    // Koin
    implementation(libs.bundles.koin)

    // Kakao
    implementation(libs.kakao)

    // Ktor
    implementation(libs.bundles.ktor)
}