plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.saiful.base_unit_test"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    lint {
        targetSdk = 35
    }
    testOptions {
        targetSdk = 35
    }
}

dependencies {
    implementation(project(":base"))

    api(libs.junit)
    api(libs.mockito.kotlin) {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-reflect")
    }
    api(libs.mockito)
    api(libs.androidx.espresso.core)
    api(libs.coroutine.test)
} 