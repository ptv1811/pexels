plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.vanluong.common"
}

dependencies {

    implementation(libs.androidx.appcompat)
    implementation(libs.recyclerView)

    // moshi
    implementation(libs.moshi)
    ksp(libs.moshi.codegen)

    // testing
    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
}