pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("com.android.settings") version "8.9.1"
}

android {
    minSdk = 28
    compileSdk = 35

}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Pexels"
include(":app")
include(":core:common")
include(":core:network")
include(":core:model")
include(":core:data")
include(":core:database")
include(":core:domain")
include(":core:testing")

include(":features:search")
include(":features:recent")
