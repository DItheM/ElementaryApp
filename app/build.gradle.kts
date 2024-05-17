plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.elementaryapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.elementaryapp"
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
    buildFeatures {
        viewBinding = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
//    composeOptions {
//        kotlinCompilerExtensionVersion = "1.3.2"
//    }

dependencies {

    implementation ("com.gorisse.thomas.sceneform:sceneform:1.23.0")
    implementation ("androidx.camera:camera-core:1.3.1")
    implementation ("androidx.camera:camera-camera2:1.3.1")
    implementation ("androidx.camera:camera-lifecycle:1.3.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
//    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

    implementation("androidx.navigation:navigation-fragment:2.7.6")
    implementation("androidx.navigation:navigation-ui:2.7.6")
    implementation("androidx.camera:camera-view:1.3.1")
//    implementation ("io.github.sceneview:arsceneview:2.0.3")
    implementation("com.google.android.gms:play-services-cast-framework:21.4.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

//    constraints {
//        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.0") {
//            because("kotlin-stdlib-jdk7 is now a part of kotlin-stdlib")
//        }
//        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0") {
//            because("kotlin-stdlib-jdk8 is now a part of kotlin-stdlib")
//        }
//    }
}
}
dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.0")

}
