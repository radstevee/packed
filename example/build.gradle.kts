plugins {
  id("com.gradleup.shadow") version "9.0.0-beta10"
  application
}

dependencies {
  api(project(":packed-core"))
  api(project(":packed-negative-spaces"))
  api(libs.kotlin.test)
  api(libs.kgit)
  api(libs.slf4j.log4j)

  runtimeClasspath(libs.dfu)
}

tasks {
  assemble {
    dependsOn(shadowJar)
  }
  shadowJar {
    version = ""
    archiveClassifier = ""
  }
}

application {
  mainClass = "net.radstevee.packed.example.MainKt"
}
