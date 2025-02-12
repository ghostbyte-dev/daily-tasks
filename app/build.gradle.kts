plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    id("com.google.devtools.ksp")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.daniebeler.dailytasks"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.daniebeler.dailytasks"
        minSdk = 26
        targetSdk = 35
        versionCode = 6
        versionName = "2.0"

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    buildFeatures {
        compose = true
    }


    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
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

}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.kotlin.bom))
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.material)
    implementation(libs.androidx.material3)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.hilt.work)
    implementation(libs.dagger.hilt)
    implementation(libs.hilt.naviation)
    implementation(libs.androidx.work.runtime.ktx)
    ksp(libs.dagger.hilt.compiler)

    // Preferences DataStore
    implementation(libs.androidx.datastore.preferences)

    implementation (libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
}