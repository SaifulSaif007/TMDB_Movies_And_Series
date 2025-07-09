plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    id("jacoco")
}

android {
    namespace = "com.saiful.tvshows"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        targetSdk = 35

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            isTestCoverageEnabled = true
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
}

dependencies {
    implementation(project(":base"))
    implementation(project(":shared"))
    testImplementation(project(":base-unit-test"))

    implementation(libs.dagger.hilt)
    implementation(libs.bundles.androidxNavigation)
    kapt(libs.hilt.compiler)
}

// Jacoco configuration for code coverage
configure<JacocoPluginExtension> {
    toolVersion = "0.8.8"
}

afterEvaluate {
    val excludes = listOf(
        "**/R.class", "**/R$*.class", "**/BuildConfig.*", "**/Manifest*.*", "**/*Test*.*",
        "android/**/*.*", "androidx/**/*.*", "**/*\$ViewInjector*.*", "**/*Dagger*.*", "**/*Hilt*.*",
        "**/hilt_aggregated*/*", "**/*MembersInjector*.*", "**/*_Factory.*", "**/*_Provide*Factory*.*",
        "**/*_ViewBinding*.*", "**/AutoValue_*.*", "**/R2.class", "**/R2$*.class", "**/*Directions$*",
        "**/*Directions.*", "**/*Binding.*", "**/mock/*", "**/remote/*", "**/di/**", "**/ui/**",
        "**/*DispatcherProvider*/", "**/model/*", "**/*Fragment*", "**/view/adapter/*", "**/navigation/*",
        "**/data/repository/paging/*", "**/view/list/*"
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
            executionData.setFrom(files("$buildDir/outputs/unit_test_code_coverage/${variantName}UnitTest/${testTaskName}.exec"))
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