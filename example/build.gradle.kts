plugins {
    alias(libs.plugins.spotless)
    alias(libs.plugins.kotlin)
}

repositories {
    mavenCentral()
    mavenLocal()
    /*maven {
        name = "rad-public"
        url = uri("https://maven.radsteve.net/public")
    }*/
}

dependencies {
    api("net.radstevee.packed:packed-core:$version")
    api("net.radstevee.packed:packed-negative-spaces:$version")
    api(libs.kotlin.test)
    api(libs.kgit)
    api(libs.slf4j.log4j)
}

tasks.test {
    useJUnitPlatform()
}
