plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.vanluong.testing"
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.coroutines.android)
    implementation(libs.coroutines.test)
    implementation(libs.junit)
}