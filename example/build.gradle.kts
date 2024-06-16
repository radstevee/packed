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
    implementation("com.github.sya-ri:kgit:1.0.6")
    implementation("org.slf4j:slf4j-log4j12:1.7.30")
}

tasks.test {
    useJUnitPlatform()
}
