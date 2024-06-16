plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.bp_buddy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.bp_buddy"
        minSdk = 26
        targetSdk = 34
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

    buildFeatures{
        dataBinding=true
        viewBinding=true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

        implementation(platform("com.google.firebase:firebase-bom:33.0.0"))

        implementation("com.google.firebase:firebase-firestore-ktx:25.0.0")
        implementation("com.google.firebase:firebase-analytics")
        implementation("com.jjoe64:graphview:4.2.2")
        implementation("com.google.firebase:firebase-auth-ktx")

        implementation("androidx.navigation:navigation-fragment-ktx:2.4.0-alpha05")
        implementation("androidx.navigation:navigation-ui-ktx:2.4.0-alpha05")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.firestore)
    implementation(libs.play.services.fitness)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}