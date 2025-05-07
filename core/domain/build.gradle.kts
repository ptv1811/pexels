plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.vanluong.domain"
}

dependencies {
    api(project(":core:model"))
    api(project(":core:data"))

    implementation(libs.javax.inject)

    // Paging
    implementation(libs.androidx.paging.common.android)
    implementation(libs.paging)

    testImplementation(project(":core:testing"))
}