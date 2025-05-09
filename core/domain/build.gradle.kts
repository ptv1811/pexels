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

    implementation(libs.javax.inject)

    // Paging
    implementation(libs.androidx.paging.common.android)
    implementation(libs.paging)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.turbine)
    implementation(libs.core.ktx)
    testImplementation(libs.coroutines.android)
    testImplementation(libs.coroutines.test)
}