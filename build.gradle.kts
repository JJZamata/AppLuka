plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    //firebase

    id("com.google.firebase.crashlytics") version "3.0.2" apply false
    id("com.google.firebase.firebase-perf") version "1.4.2" apply false
    //hilt
    id("com.google.devtools.ksp") version "1.9.0-1.0.11" apply false
    id("com.google.dagger.hilt.android") version "2.47" apply false

    // Agregar el plugin de Google Services
    id("com.google.gms.google-services") version "4.4.2" apply false

}