import com.diffplug.gradle.spotless.SpotlessPlugin

plugins {
    alias(libs.plugins.spotless)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.dokka)
    `maven-publish`
}

allprojects {
    group = "net.radstevee.packed"
    version = "0.2.4"
}

subprojects {
    apply<SpotlessPlugin>()
    apply(plugin = "kotlin")

    repositories {
        mavenCentral()

        maven {
            name = "radPublic"
            url = uri("https://maven.radsteve.net/public")

            credentials {
                username = System.getenv("RAD_MAVEN_USER")
                password = System.getenv("RAD_MAVEN_TOKEN")
            }
        }
    }
}

kotlin {
    jvmToolchain(17)
}