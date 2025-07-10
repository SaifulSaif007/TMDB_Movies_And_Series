plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.plugin)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.saiful.base"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        targetSdk = 35

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "ACCESS_TOKEN", project.property("access_token") as String)
        buildConfigField("String", "BASE_URL", project.property("base_url") as String)
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
        abortOnError = false
    }
    
    buildFeatures {
        buildConfig = true // Enable BuildConfig generation
        viewBinding = true
    }
}

dependencies {
    implementation(libs.retrofit)
    api(libs.moshi.converter)
    implementation(libs.logging.interceptor)

    implementation(libs.bundles.androidxLifecycle)

    api(libs.androidx.viewpager)

    implementation(libs.androidx.navigation.runtime)
    

    implementation(libs.dagger.hilt)
    kapt(libs.hilt.compiler)
}