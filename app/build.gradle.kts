plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    id("kotlin-kapt")

    id ("com.google.gms.google-services")


}

android {
    namespace = "fr.devmobile.protx_v2"
    compileSdk = 36

    defaultConfig {
        applicationId = "fr.devmobile.protx_v2"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding = true
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
}

dependencies {


    implementation(libs.androidx.room.runtime.v283)
    implementation(libs.androidx.room.ktx.v283)
    kapt(libs.androidx.room.compiler.v283)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(platform(libs.firebase.bom.v3320))


    // Authentification
    implementation (libs.firebase.auth)

    // Firestore (base de données Cloud)
    implementation (libs.firebase.firestore.ktx)

    // Pour les coroutines
    implementation (libs.kotlinx.coroutines.android)

}