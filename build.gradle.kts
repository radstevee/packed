import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin
import org.jetbrains.dokka.gradle.DokkaPlugin
import org.jetbrains.dokka.gradle.tasks.DokkaGeneratePublicationTask
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

plugins {
  alias(libs.plugins.spotless)
  alias(libs.plugins.kotlin)
  alias(libs.plugins.dokka)
  `maven-publish`
}

val packedVersion: String by project

allprojects {
  group = "net.radstevee.packed"
  version = packedVersion

  apply(plugin = "kotlin")
  apply(plugin = "java-library")
  apply<DokkaPlugin>()
  apply<MavenPublishPlugin>()
  apply<SpotlessPlugin>()

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
        .setEditorConfigPath(rootProject.projectDir.resolve(".editorconfig"))
    }
  }
  val sourcesJar = tasks.register<Jar>("sourcesJar") {
    from(sourceSets.main.get().allSource)
    archiveClassifier.set("sources")
  }
  val dokkaJar = tasks.register<Jar>("dokkaHtmlJar") {
    dependsOn(tasks.dokkaGeneratePublicationHtml)
    from(tasks.dokkaGeneratePublicationHtml.flatMap(DokkaGeneratePublicationTask::outputDirectory))
    archiveClassifier.set("javadoc")
  }

  configure<PublishingExtension> {
    publications {
      create<MavenPublication>("mavenJava") {
        from(components["java"])

        artifact(sourcesJar) {
          classifier = "sources"
        }

        artifact(dokkaJar) {
          classifier = "javadoc"
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

tasks.register("publishAll") {
  childProjects.filterKeys { name -> name != "example" }.forEach { (_, project) ->
    dependsOn(project.tasks.getByPath("spotlessCheck"))
    dependsOn(project.tasks.getByPath("publish"))
  }
}
