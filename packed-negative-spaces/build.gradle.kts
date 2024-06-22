plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlinx.serialization)
    `maven-publish`
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://maven.radsteve.net/public")
}

dependencies {
    api("net.radstevee.packed:packed-core:$version")
    api("org.apache.commons:commons-text:1.12.0")
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
