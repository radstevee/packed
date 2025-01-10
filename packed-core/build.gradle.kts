apply(plugin = "org.jetbrains.dokka")

plugins {
    alias(libs.plugins.kotlinx.serialization)
}

dependencies {
    testImplementation(libs.kotlin.test)
    api(libs.kotlinx.serialization.json)
    api(libs.kgit)
    api(libs.slf4j.api)
    api(libs.slf4j.log4j)
    api(libs.zip)
}

tasks.test {
    useJUnitPlatform()
}
