plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.plugin)
    alias(libs.plugins.kotlin.kapt)
    id("jacoco")
}

android {
    namespace = "com.saiful.tmdb2"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.saiful.tmdb2"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(project(":base"))
    implementation(project(":shared"))
    implementation(project(":movie"))
    implementation(project(":person"))
    implementation(project(":tvshows"))

    testImplementation(project(":base-unit-test"))

    implementation(libs.dagger.hilt)
    kapt(libs.hilt.compiler)

    implementation(libs.google.material)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}

// Jacoco configuration for code coverage
configure<JacocoPluginExtension> {
    toolVersion = "0.8.11"
}

afterEvaluate {
    val excludes = listOf(
        "**/R.class", "**/R$*.class", "**/BuildConfig.*", "**/Manifest*.*", "**/*Test*.*",
        "android/**/*.*", "androidx/**/*.*", "**/*\$ViewInjector*.*", "**/*Dagger*.*", "**/*Hilt*.*",
        "**/hilt_aggregated*/*", "**/*MembersInjector*.*", "**/*_Factory.*", "**/*_Provide*Factory*.*",
        "**/*_ViewBinding*.*", "**/AutoValue_*.*", "**/R2.class", "**/R2$*.class", "**/*Directions$*",
        "**/*Directions.*", "**/*Binding.*", "**/mock/*", "**/remote/*", "**/di/**", "**/ui/**",
        "**/*DispatcherProvider*/", "**/model/*", "**/*Fragment*", "**/*adapter*", "**/navigation/*",
        "**/data/repository/paging/*", "**/view/list/*", "**/search/*"
    )

    fun createVariantCoverage(variant: com.android.build.gradle.api.BaseVariant) {
        val variantName = variant.name
        val testTaskName = "test${variantName.replaceFirstChar { it.uppercase() }}UnitTest"

        tasks.register("${testTaskName}Coverage", JacocoReport::class.java) {
            group = "Reporting"
            description = "Generate Jacoco coverage reports for the ${variantName} build."
            dependsOn(testTaskName)

            reports {
                xml.required.set(true)
                html.required.set(true)
                csv.required.set(false)
            }

            val javaClasses = fileTree(variant.javaCompileProvider.get().destinationDirectory.get().asFile) {
                exclude(excludes)
            }
            val kotlinClasses = fileTree("$buildDir/tmp/kotlin-classes/$variantName") {
                exclude(excludes)
            }

            classDirectories.setFrom(files(javaClasses, kotlinClasses))
            sourceDirectories.setFrom(
                files(
                    "$projectDir/src/main/java",
                    "$projectDir/src/$variantName/java",
                    "$projectDir/src/main/kotlin",
                    "$projectDir/src/$variantName/kotlin"
                )
            )
            executionData.setFrom(files("$buildDir/jacoco/${testTaskName}.exec"))
        }

        tasks.register("${testTaskName}CoverageVerification", JacocoCoverageVerification::class.java) {
            group = "Reporting"
            description = "Verifies Jacoco coverage for the ${variantName} build."
            dependsOn("${testTaskName}Coverage")

            violationRules {
                rule {
                    limit {
                        minimum = "0.0".toBigDecimal()
                    }
                }
                rule {
                    element = "BUNDLE"
                    limit {
                        counter = "LINE"
                        value = "COVEREDRATIO"
                        minimum = "0.30".toBigDecimal()
                    }
                }
            }

            val javaClasses = fileTree(variant.javaCompileProvider.get().destinationDirectory.get().asFile) {
                exclude(excludes)
            }
            val kotlinClasses = fileTree("$buildDir/tmp/kotlin-classes/$variantName") {
                exclude(excludes)
            }

            classDirectories.setFrom(files(javaClasses, kotlinClasses))
            sourceDirectories.setFrom(
                files(
                    "$projectDir/src/main/java",
                    "$projectDir/src/$variantName/java",
                    "$projectDir/src/main/kotlin",
                    "$projectDir/src/$variantName/kotlin"
                )
            )
            executionData.setFrom(files("$buildDir/outputs/unit_test_code_coverage/${variantName}UnitTest/${testTaskName}.exec"))
        }
    }

    val androidExtension = extensions.findByName("android")
    val variants = when (androidExtension) {
        is com.android.build.gradle.AppExtension -> androidExtension.applicationVariants
        is com.android.build.gradle.LibraryExtension -> androidExtension.libraryVariants
        else -> null
    }
    variants?.forEach { variant -> createVariantCoverage(variant) }
}