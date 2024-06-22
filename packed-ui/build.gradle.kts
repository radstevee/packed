plugins {
    alias(libs.plugins.spotless)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.dokka)
    `maven-publish`
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.radsteve.net/public")
}

dependencies {
    compileOnly(libs.paper.api)
    api("net.radstevee.packed:packed-core:$version")
    api("net.radstevee.packed:packed-negative-spaces:$version")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }

    repositories {
        maven {
            name = "radPublic"
            url = uri("https://maven.radsteve.net/public")

            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("RAD_MAVEN_USER")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("RAD_MAVEN_TOKEN")
            }
        }
    }
}
