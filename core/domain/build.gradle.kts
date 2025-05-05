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
    testImplementation(project(":core:testing"))
}