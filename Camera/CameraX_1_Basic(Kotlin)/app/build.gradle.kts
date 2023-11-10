plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.camerax_1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.camerax_1"
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
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    // CameraX core library using the camera2 implementation
    val camerax_version = "1.4.0-alpha02"

    // 다음 라인은 선택 사항이며, 핵심 라이브러리는 간접적으로 camera-camera2에 의해 포함됩니다.
    implementation("androidx.camera:camera-core:${camerax_version}")
    implementation("androidx.camera:camera-camera2:${camerax_version}")

    // CameraX Lifecycle 라이브러리를 추가로 사용하려면 추가하세요
    implementation("androidx.camera:camera-lifecycle:${camerax_version}")

    // CameraX VideoCapture 라이브러리를 추가로 사용하려면 추가하세요.
    implementation("androidx.camera:camera-video:${camerax_version}")

    // CameraX View 클래스를 추가로 사용하려면 추가하세요.
    implementation("androidx.camera:camera-view:${camerax_version}")

    // CameraX ML Kit Vision 통합을 추가로 사용하려면 추가하세요.
    implementation("androidx.camera:camera-mlkit-vision:${camerax_version}")

    // CameraX Extensions 라이브러리를 추가적으로 사용하려면 추가하세요.
    implementation("androidx.camera:camera-extensions:${camerax_version}")
}