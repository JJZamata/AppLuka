plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    //Procesamiento de anotaciones en Kotlin
    id("com.google.devtools.ksp")
    //inyeccion de dependencias
    id("dagger.hilt.android.plugin")
    // Agregar el plugin de Google Services
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.puyodev.luka"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.puyodev.luka"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            // Este bloque se puede dejar vacío o personalizar según sea necesario
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
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
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.common.ktx)
    implementation(libs.androidx.material3.android)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.config.ktx)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.navigation.compose.v274)//para la navegacion entre pantallas
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.firebase.firestore.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Agregar Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-perf")
    implementation("com.google.firebase:firebase-config")
    implementation("com.google.firebase:firebase-messaging")

    // Agregar Firebase Analytics (u otros servicios de Firebase que desees)
    implementation("com.google.firebase:firebase-analytics")

    // Dependencias adicionales de Firebase que puedas necesitar

    //inyeccion de dependencias - hilt
    implementation("com.google.dagger:hilt-android:2.47")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    //- compilador
    ksp("com.google.dagger:hilt-compiler:2.47")
}