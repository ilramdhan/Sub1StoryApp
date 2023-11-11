plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
    id ("com.google.dagger.hilt.android")
    id ("androidx.navigation.safeargs")
    id ("kotlin-parcelize")
}

android {
    namespace = "com.dicoding.sub1storyapp"
    compileSdk = 34

    defaultConfig {
        buildConfigField("String", "BASE_URL", "\"https://story-api.dicoding.dev/v1/\"")
        applicationId = "com.dicoding.sub1storyapp"
        minSdk = 26
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    val hiltVer = "2.48.1"
    implementation ("com.google.dagger:hilt-android:$hiltVer")
    kapt ("com.google.dagger:hilt-compiler:$hiltVer")
    kapt ("com.google.dagger:dagger-compiler:$hiltVer")
    kapt ("com.google.dagger:dagger-android-processor:2.35.1")

    testImplementation ("com.google.dagger:hilt-android-testing:$hiltVer")
    testAnnotationProcessor ("com.google.dagger:hilt-compiler:$hiltVer")

    implementation ("com.google.code.gson:gson:2.9.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation ("com.squareup.okhttp3:logging-interceptor:4.11.0")

    implementation ("com.github.bumptech.glide:glide:4.13.2")

    implementation ("com.jakewharton.timber:timber:5.0.1")

    val cameraxVer = "1.4.0-alpha02"
    implementation ("androidx.camera:camera-camera2:$cameraxVer")
    implementation ("androidx.camera:camera-lifecycle:$cameraxVer")
    implementation ("androidx.camera:camera-view:$cameraxVer")

    implementation ("de.hdodenhof:circleimageview:3.1.0")

    implementation ("androidx.room:room-runtime:2.6.0")
    kapt ("androidx.room:room-compiler:2.6.0")
    implementation ("androidx.room:room-ktx:2.6.0")

    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.4")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.4")

    implementation ("androidx.fragment:fragment-ktx:1.6.1")

    implementation ("com.facebook.shimmer:shimmer:0.5.0")
}