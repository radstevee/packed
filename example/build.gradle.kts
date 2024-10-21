plugins {
    alias(libs.plugins.spotless)
    alias(libs.plugins.kotlin)
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":packed-core"))
    api(project(":packed-negative-spaces"))
    api(libs.kotlin.test)
    api(libs.kgit)
    api(libs.slf4j.log4j)
}
