// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.5.21"
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
}