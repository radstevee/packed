import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin
import org.jetbrains.dokka.gradle.DokkaPlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

plugins {
    alias(libs.plugins.spotless)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.dokka)
    `maven-publish`
}

allprojects {
    group = "net.radstevee.packed"
    version = "1.0.0-SNAPSHOT.3"

    apply(plugin = "kotlin")
    apply(plugin = "maven-publish")
    apply<SpotlessPlugin>()
    apply<DokkaPlugin>()

    repositories {
        mavenCentral()
        maven("https://libraries.minecraft.net")
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
    val sourcesJar = tasks.register<Jar>("sourcesJar") {
        from(sourceSets.main.get().allSource)
        archiveClassifier.set("sources")
    }

    configure<PublishingExtension> {
        publications {
            create<MavenPublication>("mavenJava") {
                from(components["java"])

                artifact(sourcesJar) {
                    classifier = "sources"
                }
            }
        }

        repositories {
            maven {
                name = "rad-public"
                url = uri("https://maven.radsteve.net/public")

                credentials {
                    username = System.getenv("RAD_MAVEN_USER")
                    password = System.getenv("RAD_MAVEN_TOKEN")
                }
            }
        }
    }
}

kotlin {
    jvmToolchain(21)
}

tasks.register("publishAll") {
    childProjects.filterKeys { name -> name != "example" }.forEach { (_, project) ->
        dependsOn(project.tasks.getByPath("publish"))
    }
}
