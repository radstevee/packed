plugins {
    kotlin("jvm")
}

group = "net.radstevee.packed"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()

    maven {
        name = "rad-public"
        url = uri("https://maven.radsteve.net/public")
    }
}

dependencies {
    implementation("net.radstevee:packed:1.0-SNAPSHOT")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
