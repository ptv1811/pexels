plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.vanluong.pexels"

    buildFeatures {
        dataBinding = true
        buildConfig = true
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
}

dependencies {
    implementation(project(":features:search"))

    implementation(project(":core:data"))
    implementation(project(":core:model"))
    implementation(project(":core:common"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.recyclerView)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Navigation
    api(libs.androidx.navigation.fragment.ktx)
    api(libs.androidx.navigation.ui.ktx)
    api(libs.androidx.navigation.dynamic.features.fragment)

    // Viewmodel
    implementation(libs.androidx.lifecyle.viewmodel.ktx)
    implementation(libs.android.lifecycle.savedstate)

    // hilt
    implementation(libs.android.hilt)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.android.processor)

    // network
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.okhttp.logging.interceptor)

    // Room
    implementation(libs.android.room)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // coroutines
    implementation(libs.coroutines.android)

    // moshi
    implementation(libs.moshi)

    // Glide
    implementation(libs.glide)
    ksp(libs.glide.ksp)
}