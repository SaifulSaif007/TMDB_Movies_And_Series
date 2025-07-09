// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt.plugin) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.navigation.safeargs) apply false
}

// Project-level JaCoCo configuration for consolidated reports
allprojects {
    apply(plugin = "jacoco")
    
    configure<JacocoPluginExtension> {
        toolVersion = "0.8.11"
    }
}

// Consolidated JaCoCo report task
tasks.register("jacocoConsolidatedReport", JacocoReport::class.java) {
    group = "Reporting"
    description = "Generate consolidated JaCoCo coverage report for all modules"
    
    // Depend on all test tasks from all modules
    dependsOn(subprojects.map { it.tasks.withType<Test>() })
    
    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }
    
    // Collect execution data from all modules
    val executionDataFiles = subprojects.map { project ->
        project.fileTree(project.layout.buildDirectory) {
            include("jacoco/*.exec")
            include("outputs/unit_test_code_coverage/*/test*UnitTest.exec")
        }
    }.flatten()
    executionData.setFrom(executionDataFiles)

    // Only include main source set class directories for each module
    val classDirectoriesFiles = subprojects.map { project ->
        listOf(
            project.layout.buildDirectory.dir("intermediates/javac/debug/classes").get().asFile,
            project.layout.buildDirectory.dir("tmp/kotlin-classes/debug").get().asFile
        ).mapNotNull { dir ->
            if (dir.exists()) project.fileTree(dir) {
                exclude(
                    "**/R.class", "**/R$*.class", "**/BuildConfig.*", "**/Manifest*.*", "**/*Test*.*",
                    "android/**/*.*", "androidx/**/*.*", "**/*\$ViewInjector*.*", "**/*Dagger*.*", "**/*Hilt*.*",
                    "**/hilt_aggregated*/*", "**/*MembersInjector*.*", "**/*_Factory.*", "**/*_Provide*Factory*.*",
                    "**/*_ViewBinding*.*", "**/AutoValue_*.*", "**/R2.class", "**/R2$*.class", "**/*Directions$*",
                    "**/*Directions.*", "**/*Binding.*", "**/mock/*", "**/remote/*", "**/di/**", "**/ui/**",
                    "**/*DispatcherProvider*/", "**/model/*", "**/*Fragment*", "**/*adapter*", "**/navigation/*",
                    "**/data/repository/paging/*", "**/view/list/*", "**/search/*"
                )
            } else null
        }
    }.flatten()
    classDirectories.setFrom(classDirectoriesFiles)

    // Collect source directories from all modules
    val sourceDirectoriesFiles = subprojects.map { project ->
        project.fileTree(project.projectDir) {
            include("src/main/java/**/*.java")
            include("src/main/java/**/*.kt")
            include("src/main/kotlin/**/*.kt")
        }
    }.flatten()
    sourceDirectories.setFrom(sourceDirectoriesFiles)
}

// Consolidated JaCoCo coverage verification task
tasks.register("jacocoConsolidatedCoverageVerification", JacocoCoverageVerification::class.java) {
    group = "Reporting"
    description = "Verifies consolidated JaCoCo coverage for all modules"
    dependsOn("jacocoConsolidatedReport")
    
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
    
    // Collect execution data from all modules
    val executionDataFiles = subprojects.map { project ->
        project.fileTree(project.layout.buildDirectory) {
            include("jacoco/*.exec")
            include("outputs/unit_test_code_coverage/*/test*UnitTest.exec")
        }
    }.flatten()
    
    executionData.setFrom(executionDataFiles)
    
    // Collect class directories from all modules
    val classDirectoriesFiles = subprojects.map { project ->
        project.fileTree(project.layout.buildDirectory) {
            include("intermediates/javac/*/classes/**/*.class")
            include("tmp/kotlin-classes/*/**/*.class")
            exclude(
                "**/R.class", "**/R$*.class", "**/BuildConfig.*", "**/Manifest*.*", "**/*Test*.*",
                "android/**/*.*", "androidx/**/*.*", "**/*\$ViewInjector*.*", "**/*Dagger*.*", "**/*Hilt*.*",
                "**/hilt_aggregated*/*", "**/*MembersInjector*.*", "**/*_Factory.*", "**/*_Provide*Factory*.*",
                "**/*_ViewBinding*.*", "**/AutoValue_*.*", "**/R2.class", "**/R2$*.class", "**/*Directions$*",
                "**/*Directions.*", "**/*Binding.*", "**/mock/*", "**/remote/*", "**/di/**", "**/ui/**",
                "**/*DispatcherProvider*/", "**/model/*", "**/*Fragment*", "**/*adapter*", "**/navigation/*",
                "**/data/repository/paging/*", "**/view/list/*", "**/search/*"
            )
        }
    }.flatten()
    
    classDirectories.setFrom(classDirectoriesFiles)
    
    // Collect source directories from all modules
    val sourceDirectoriesFiles = subprojects.map { project ->
        project.fileTree(project.projectDir) {
            include("src/main/java/**/*.java")
            include("src/main/java/**/*.kt")
            include("src/main/kotlin/**/*.kt")
        }
    }.flatten()
    
    sourceDirectories.setFrom(sourceDirectoriesFiles)
}