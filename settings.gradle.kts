pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
        google()
        maven { url = uri("https://jitpack.io") }
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        google()
        mavenCentral()
        maven { url = uri("https://maven.neshan.org/artifactory/public-maven") }
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "clean_mvvm_mvi"
include(":app")
include(":core:domain")
include(":core:network")
include(":core:database")
include(":core:session")
include(":core:location")
include(":core:common")
include(":core:ui")
include(":core:designsystem")
include(":core:navigation")
include(":feature:auth")
