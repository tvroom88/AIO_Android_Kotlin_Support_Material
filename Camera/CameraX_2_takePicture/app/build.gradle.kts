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
    val cameraxVersion = "1.4.0-alpha02"

    // CameraX의 핵심 기능을 포함합니다. 카메라 촬영을 위한 기본 기능을 제공합니다.
    implementation("androidx.camera:camera-core:${cameraxVersion}")

    // CameraX가 Android Camera2 API를 기반으로 구축되었기 때문에 이 모듈은 Camera2 API와의 통합을 지원합니다.
    implementation("androidx.camera:camera-camera2:${cameraxVersion}")

    // Android Lifecycle 라이브러리와 통합하여 카메라 수명 주기를 관리하는 기능을 제공합니다. 이 모듈을 사용하면 카메라 세션을 더 쉽게 관리할 수 있습니다.
    implementation("androidx.camera:camera-lifecycle:${cameraxVersion}")

    // 이 모듈은 비디오 촬영과 관련된 기능을 지원합니다. 비디오 캡처와 녹화를 위한 클래스 및 도구를 제공합니다.
    implementation("androidx.camera:camera-video:${cameraxVersion}")

    // 이 모듈은 CameraView와 관련된 클래스 및 위젯을 제공합니다. CameraView를 사용하면 앱에서 미리보기 화면을 표시하고 사용자 인터페이스와 통합하기가 더 쉬워집니다.
    implementation("androidx.camera:camera-view:${cameraxVersion}")

    // ML Kit Vision과 통합하여 기계 학습을 사용한 컴퓨터 비전 작업에 카메라를 쉽게 통합할 수 있도록 돕습니다. 이 모듈은 ML Kit Vision API와의 통합을 지원합니다.
    implementation("androidx.camera:camera-mlkit-vision:${cameraxVersion}")

    // CameraX 확장 모듈을 포함하며, 다양한 확장을 통해 추가적인 기능과 기능을 확장할 수 있습니다.
    implementation("androidx.camera:camera-extensions:${cameraxVersion}")
}