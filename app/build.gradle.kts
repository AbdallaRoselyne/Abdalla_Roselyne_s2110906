plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.abdalla_roselyne_s2110906"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.abdalla_roselyne_s2110906"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    // Existing dependencies
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Retrofit and Gson converter for parsing JSON
    implementation("androidx.annotation:annotation:1.7.1")
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("androidx.cardview:cardview:1.0.0")
}
