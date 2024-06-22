plugins {
    alias(libs.plugins.spotless)
    alias(libs.plugins.kotlin)
    id("xyz.jpenilla.run-paper") version "2.3.0"
    id("io.github.goooler.shadow") version "8.1.7"
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.radsteve.net/public")
}

dependencies {
    compileOnly(libs.paper.api)
    implementation("net.radstevee.packed:packed-core:$version")
    implementation("net.radstevee.packed:packed-negative-spaces:$version")
    implementation("net.radstevee.packed:packed-ui:$version")
}

tasks {
    runServer {
        minecraftVersion("1.20.6")
    }
}