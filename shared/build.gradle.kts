import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("app.cash.sqldelight")
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21"
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
            linkerOpts("-lsqlite3")
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
            implementation("app.cash.sqldelight:runtime:2.0.2")
            implementation("app.cash.sqldelight:coroutines-extensions:2.0.2")
            implementation("com.russhwolf:multiplatform-settings:1.3.0")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")
            implementation("io.insert-koin:koin-core:3.5.6")
        }

        androidMain.dependencies {
            implementation("app.cash.sqldelight:android-driver:2.0.2")
            implementation("io.insert-koin:koin-android:3.5.6")
            implementation("org.mindrot:jbcrypt:0.4")
        }

        iosMain.dependencies {
            implementation("app.cash.sqldelight:native-driver:2.0.2")
        }
    }
}

android {
    namespace = "com.duka.shared"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

sqldelight {
    databases {
        create("DukaDatabase") {
            packageName.set("com.duka.shared.db")
        }
    }
}
