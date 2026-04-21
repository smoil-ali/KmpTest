import org.gradle.api.tasks.Exec
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.CInteropProcess

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    kotlin("plugin.serialization") version "1.9.20"
    alias(libs.plugins.sqlDelight)
}

/** iOS / Kotlin Native: pass `-Pkmp.environment=staging` (default `live`). Matches Android `staging` flavor URLs. */
val kmpEnvironment: String =
    when (findProperty("kmp.environment")?.toString()?.lowercase()?.trim()) {
        "staging" -> "staging"
        else -> "live"
    }

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosArm64{
            binaries.all {
                linkerOpts("-lsqlite3")
            }
        },
        iosSimulatorArm64 {
            binaries.all {
                linkerOpts("-lsqlite3")
            }
        }
    ).forEach { iosTarget ->
        val nativelibLibDir =
            layout.buildDirectory
                .dir("native/libs/$kmpEnvironment/${iosTarget.name}")
                .get()
                .asFile
                .absolutePath
        val nativelibDef =
            layout.buildDirectory
                .dir("generated/nativeInterop")
                .get()
                .asFile
                .apply { mkdirs() }
                .resolve("nativelib_ios_${iosTarget.name}_$kmpEnvironment.def")
                .apply {
                    writeText(
                        """
                        headers = kmp_native_urls.h
                        package = com.appswallet.kmptest.native.interop
                        staticLibraries = libnativelib_ios.a
                        libraryPaths = $nativelibLibDir
                        """.trimIndent(),
                    )
                }
        iosTarget.compilations.getByName("main") {
            cinterops.register("nativelibIos") {
                definitionFile.set(nativelibDef)
                compilerOpts(
                    "-I${rootProject.layout.projectDirectory.dir("nativeCommon").asFile.absolutePath}",
                )
            }
        }
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sqldelight {
        databases {
            create(name = "KmpDb") {
                packageName.set("com.appswallet.kmptest.db")
            }
        }
        linkSqlite.set(true)
    }

    sourceSets {

        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.lifecycle.viewmodel.ktx)
            implementation(libs.ktor.client.android)
            implementation(libs.koin.android)
            implementation(libs.sql.android.driver)
            implementation(libs.sqldelight.runtime)
            implementation(projects.nativelib)



        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.sql.coroutines.extensions)


        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.sql.native.driver)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.appswallet.kmptest"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.appswallet.kmptest"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    flavorDimensions += "environment"
    productFlavors {
        create("live") {
            dimension = "environment"
            isDefault = true
        }
        create("staging") {
            dimension = "environment"
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}


dependencies {
    debugImplementation(libs.compose.uiTooling)
}

val iosNativelibStagingFlag: String =
    if (kmpEnvironment == "staging") {
        "-DSTAGING=1 "
    } else {
        ""
    }

tasks.register<Exec>("compileNativelibIosDevice") {
    group = "build"
    description =
        "Build libnativelib_ios.a for Kotlin/Native cinterop (iosArm64, kmp.environment=$kmpEnvironment)"
    workingDir = projectDir
    inputs.property("kmp.environment", kmpEnvironment)
    commandLine(
        "/bin/bash",
        "-c",
        "set -euo pipefail && " +
            "mkdir -p build/native/libs/$kmpEnvironment/iosArm64 && " +
            "SDK=${'$'}(xcrun --sdk iphoneos --show-sdk-path) && " +
            "xcrun --sdk iphoneos clang++ -arch arm64 -std=c++17 -isysroot \"${'$'}SDK\" " +
            iosNativelibStagingFlag +
            " -c ../nativeCommon/kmp_native_urls.cpp " +
            "-o build/native/libs/$kmpEnvironment/iosArm64/nativelib_ios.o && " +
            "xcrun --sdk iphoneos ar rcs build/native/libs/$kmpEnvironment/iosArm64/libnativelib_ios.a " +
            "build/native/libs/$kmpEnvironment/iosArm64/nativelib_ios.o",
    )
    inputs.file(rootProject.layout.projectDirectory.file("nativeCommon/kmp_native_urls.cpp"))
    inputs.file(rootProject.layout.projectDirectory.file("nativeCommon/kmp_native_urls.h"))
    outputs.file(layout.buildDirectory.file("native/libs/$kmpEnvironment/iosArm64/libnativelib_ios.a"))
}

tasks.register<Exec>("compileNativelibIosSimulator") {
    group = "build"
    description =
        "Build libnativelib_ios.a for Kotlin/Native cinterop (iosSimulatorArm64, kmp.environment=$kmpEnvironment)"
    workingDir = projectDir
    inputs.property("kmp.environment", kmpEnvironment)
    commandLine(
        "/bin/bash",
        "-c",
        "set -euo pipefail && " +
            "mkdir -p build/native/libs/$kmpEnvironment/iosSimulatorArm64 && " +
            "SDK=${'$'}(xcrun --sdk iphonesimulator --show-sdk-path) && " +
            "xcrun --sdk iphonesimulator clang++ -arch arm64 -std=c++17 -isysroot \"${'$'}SDK\" " +
            iosNativelibStagingFlag +
            " -c ../nativeCommon/kmp_native_urls.cpp " +
            "-o build/native/libs/$kmpEnvironment/iosSimulatorArm64/nativelib_ios.o && " +
            "xcrun --sdk iphonesimulator ar rcs build/native/libs/$kmpEnvironment/iosSimulatorArm64/libnativelib_ios.a " +
            "build/native/libs/$kmpEnvironment/iosSimulatorArm64/nativelib_ios.o",
    )
    inputs.file(rootProject.layout.projectDirectory.file("nativeCommon/kmp_native_urls.cpp"))
    inputs.file(rootProject.layout.projectDirectory.file("nativeCommon/kmp_native_urls.h"))
    outputs.file(layout.buildDirectory.file("native/libs/$kmpEnvironment/iosSimulatorArm64/libnativelib_ios.a"))
}

tasks.withType<CInteropProcess>().configureEach {
    val n = name
    if (!n.contains("nativelibIos", ignoreCase = true)) return@configureEach
    when {
        n.contains("Simulator", ignoreCase = true) -> dependsOn("compileNativelibIosSimulator")
        else -> dependsOn("compileNativelibIosDevice")
    }
}

