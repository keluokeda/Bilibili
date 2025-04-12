plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.0.21"
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.ke.bilibili.tv"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ke.bilibili.tv"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

    }

    signingConfigs {
        create("release") {
            storeFile = file("../123456")
            storePassword = "123456"
            keyAlias = "key0"
            keyPassword = "123456"
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
        debug {
            signingConfig = signingConfigs.getByName("release")
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
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.tv.foundation)
    implementation(libs.androidx.tv.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(project(":api"))
    implementation(project(":common"))
    implementation(project(":viewModel"))
    implementation(project(":db"))

    val nav_version = "2.8.9"
    // Jetpack Compose integration
    implementation("androidx.navigation:navigation-compose:$nav_version")
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    val paging_version = "3.3.6"
    implementation("androidx.paging:paging-runtime:$paging_version")
    implementation("androidx.paging:paging-compose:3.3.6")

    implementation("io.coil-kt:coil-compose:2.7.0")

    implementation("androidx.compose.material:material-icons-extended")

//    implementation("io.sanghun:compose-video:1.2.0")
    implementation("androidx.media3:media3-exoplayer:1.6.0") // [Required] androidx.media3 ExoPlayer dependency
    implementation("androidx.media3:media3-session:1.6.0") // [Required] MediaSession Extension dependency
    implementation("androidx.media3:media3-ui:1.6.0") // [Required] Base Player UI

    implementation("androidx.media3:media3-exoplayer-dash:1.6.0") // [Optional] If your media item is DASH
    implementation("androidx.media3:media3-exoplayer-hls:1.6.0") // [Optional] If your media item is HLS (m3u8..)

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    implementation("com.orhanobut:logger:2.2.0")

    implementation("io.github.2307vivek:seeker:1.2.2")

    implementation("com.lightspark:compose-qr-code:1.0.1")
}