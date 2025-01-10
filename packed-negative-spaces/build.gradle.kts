plugins {
    alias(libs.plugins.kotlinx.serialization)
}

dependencies {
    api(project(":packed-core"))
    api("org.apache.commons:commons-text:1.12.0")
}
