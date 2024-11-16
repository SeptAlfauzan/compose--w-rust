import com.android.build.gradle.tasks.MergeSourceSetFolders
import com.nishtahir.CargoBuildTask

plugins {
    kotlin("plugin.serialization") version "1.9.0" apply true
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.mozilla.rust) apply true
}


val rustDirName = "rust-bridge"
val libName = "sample"
cargo {
    module = "../$rustDirName"
    libname = libName
    targets = listOf("arm64", "x86_64")
    // Add these for debugging
    verbose = true
}

val task = tasks.register<Exec>("uniffiBindgen") {
    workingDir = file("${project.rootDir}/$rustDirName")
    commandLine("cargo", "run", "--bin", "uniffi-bindgen",
        "generate",
        "--library", "${project.rootDir}/app/build/rustJniLibs/android/arm64-v8a/libsample.so",//make sure .so name is like this lib<libname>.so
        "--language", "kotlin", "--out-dir", layout.buildDirectory.dir("generated/kotlin").get().asFile.path)
}

project.afterEvaluate {
    tasks.withType(CargoBuildTask::class)
        .forEach { buildTask ->
            tasks.withType(MergeSourceSetFolders::class)
                .configureEach {
                    this.inputs.dir(
                        layout.buildDirectory.dir("rustJniLibs" + File.separatorChar + buildTask.toolchain!!.folder)
                    )
                    this.dependsOn(buildTask)
                }
        }
}

tasks.preBuild.configure {
    dependsOn.add(tasks.withType(CargoBuildTask::class.java))
    dependsOn.add(task)
}

android {
    namespace = "org.kudos.rustoncompose"
    compileSdk = 35
    ndkVersion = "28.0.12433566"
    sourceSets {
        getByName("main").java.srcDir("build/generated/kotlin")
    }

    defaultConfig {
        applicationId = "org.kudos.rustoncompose"
        minSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
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
    implementation(libs.jna) {
        artifact {
            extension = "aar"
            type = "aar"
        }
    }
}
