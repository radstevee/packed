import com.diffplug.gradle.spotless.SpotlessPlugin
import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
    alias(libs.plugins.spotless)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.dokka)
    `maven-publish`
}

allprojects {
    group = "net.radstevee.packed"
    version = "0.3.2"
}

allprojects {
    apply<SpotlessPlugin>()
    apply(plugin = "kotlin")
    apply(plugin = "org.jetbrains.dokka")

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

    tasks.withType<DokkaTaskPartial>().configureEach {
        outputDirectory.set(File(layout.projectDirectory.asFile.parentFile, "build/docs"))
    }
}

kotlin {
    jvmToolchain(17)
}

