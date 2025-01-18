apply(plugin = "org.jetbrains.dokka")

dependencies {
    testImplementation(libs.kotlin.test)
    api(libs.kgit)
    api(libs.slf4j.api)
    api(libs.slf4j.log4j)
    api(libs.zip)
    api(libs.dfu)
}

tasks.test {
    useJUnitPlatform()
}
