plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    kotlin("plugin.serialization") version "2.0.21"
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.protobuf") version ("0.9.4")
}

android {
    namespace = "com.ke.biliblli.common"
    compileSdk = 35

    defaultConfig {
        minSdk = 21

        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        }

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
    api(project(":api"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.converter.kotlinx.serialization)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation(libs.retrofit)
    implementation("com.squareup.retrofit2:converter-protobuf:2.11.0")
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))
    // define any required OkHttp artifacts without version
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    implementation("com.github.franmontiel:PersistentCookieJar:v1.0.1")

//    api("com.tencent.bugly:crashreport:latest.releas")

    api("org.greenrobot:eventbus:3.3.1")

//    implementation("com.google.protobuf:protobuf-java:4.26.1")

//    implementation("com.google.protobuf:protobuf-javalite:3.18.0")

}
protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.21.6"
    }

    // Generates the java Protobuf-lite code for the Protobufs in this project. See
    // https://github.com/google/protobuf-gradle-plugin#customizing-protobuf-compilation
    // for more information.
//    generateProtoTasks {
//        all().each { task ->
//            task.builtins {
//                java {
//    option 'lite'
//
//                }
//            }
//        }
//    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
//                    option("lite")
                }
            }
        }
    }

}
