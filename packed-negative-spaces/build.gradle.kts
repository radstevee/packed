apply(plugin = "org.jetbrains.dokka")

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

tasks.register<Jar>("sourcesJar") {
    from(sourceSets.main.get().allSource)
    archiveClassifier.set("sources")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            artifact(tasks["sourcesJar"]) {
                classifier = "sources"
            }
        }
    }

    repositories {
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
