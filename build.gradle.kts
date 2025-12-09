// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
}

buildscript {
    dependencies {
        // Убедимся, что Hilt использует корректную версию JavaPoet
        classpath("com.squareup:javapoet:1.13.0")
    }
}

allprojects {
    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "com.squareup" && requested.name == "javapoet") {
                useVersion("1.13.0")
                because("Hilt 2.54 требует JavaPoet >= 1.13.0")
            }
        }
    }
}