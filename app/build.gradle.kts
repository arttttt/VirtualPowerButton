plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

// Release signing is read from gradle properties (e.g. ~/.gradle/gradle.properties)
// or environment variables. When no keystore is configured the release build is
// produced unsigned so the build still succeeds (e.g. on CI without secrets).
val releaseStoreFile = (project.findProperty("RELEASE_STORE_FILE") as String?)
    ?: System.getenv("RELEASE_STORE_FILE")
val releaseStorePassword = (project.findProperty("RELEASE_STORE_PASSWORD") as String?)
    ?: System.getenv("RELEASE_STORE_PASSWORD")
val releaseKeyAlias = (project.findProperty("RELEASE_KEY_ALIAS") as String?)
    ?: System.getenv("RELEASE_KEY_ALIAS")
val releaseKeyPassword = (project.findProperty("RELEASE_KEY_PASSWORD") as String?)
    ?: System.getenv("RELEASE_KEY_PASSWORD")
val hasReleaseKeystore = releaseStoreFile != null && file(releaseStoreFile).exists()

android {
    namespace = "com.arttttt.virtualpowerbutton"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.arttttt.virtualpowerbutton"
        minSdk = 29
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
    }

    signingConfigs {
        if (hasReleaseKeystore) {
            create("release") {
                storeFile = file(releaseStoreFile!!)
                storePassword = releaseStorePassword
                keyAlias = releaseKeyAlias
                keyPassword = releaseKeyPassword
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = if (hasReleaseKeystore) {
                signingConfigs.getByName("release")
            } else {
                null
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
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
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}