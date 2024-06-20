plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()

    maven {
        name = "rad-public"
        url = uri("https://maven.radsteve.net/public")
    }
}

dependencies {
    api("net.radstevee.packed:packed-core:$version")
    api(libs.kotlin.test)
    api(libs.kgit)
    api(libs.slf4j.log4j)
}

tasks.test {
    useJUnitPlatform()
}
