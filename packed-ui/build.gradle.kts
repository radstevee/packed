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