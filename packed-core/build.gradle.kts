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
