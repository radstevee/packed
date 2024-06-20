plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlinx.serialization)
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.kotlin.test)
    api(libs.kotlinx.serialization.json)
    api(libs.kgit)
    api(libs.slf4j.api)
    api(libs.slf4j.log4j)
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
