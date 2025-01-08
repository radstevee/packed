import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

plugins {
    alias(libs.plugins.spotless)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.dokka)
    `maven-publish`
}

allprojects {
    group = "net.radstevee.packed"
    version = "0.5.1"

    apply<SpotlessPlugin>()
    apply(plugin = "kotlin")
    apply(plugin = "org.jetbrains.dokka")

    repositories {
        mavenCentral()

        maven {
            name = "rad_public"
            url = uri("https://maven.radsteve.net/public")

            credentials {
                username = System.getenv("RAD_MAVEN_USER")
                password = System.getenv("RAD_MAVEN_TOKEN")
            }
        }
    }

    configure<KotlinJvmProjectExtension> {
        jvmToolchain(21)
        explicitApi()
    }

    configure<SpotlessExtension> {
        kotlin {
            ktlint("1.5.0")
        }
    }
}

kotlin {
    jvmToolchain(21)
}
