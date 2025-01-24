dependencies {
    testImplementation(libs.kotlin.test)
    api(libs.zip)
    api(libs.jetbrains.annotations)
    api(libs.kgit)
    api(libs.slf4j.api)
    api(libs.slf4j.log4j)
    api(libs.dfu)
}

tasks.test {
    useJUnitPlatform()
}
