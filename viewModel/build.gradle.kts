plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.ke.biliblli.viewmodel"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(project(":repository"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    val paging_version = "3.3.6"
    implementation("androidx.paging:paging-runtime:$paging_version")

//    implementation(project(":api"))
    implementation(project(":common"))
    implementation(project(":db"))

    val nav_version = "2.8.9"
    // Jetpack Compose integration
    implementation("androidx.navigation:navigation-compose:$nav_version")


    implementation("androidx.media3:media3-exoplayer:1.6.0") // [Required] androidx.media3 ExoPlayer dependency
    implementation("androidx.media3:media3-session:1.6.0") // [Required] MediaSession Extension dependency
    implementation("androidx.media3:media3-ui:1.6.0") // [Required] Base Player UI

    implementation("androidx.media3:media3-exoplayer-dash:1.6.0") // [Optional] If your media item is DASH
    implementation("androidx.media3:media3-exoplayer-hls:1.6.0") // [Optional] If you

    implementation("com.orhanobut:logger:2.2.0")

}