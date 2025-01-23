dependencies {
    testImplementation(libs.kotlin.test)
    implementation(libs.zip)
    implementation(libs.jetbrains.annotations)
    api(libs.kgit)
    api(libs.slf4j.api)
    api(libs.slf4j.log4j)
    api(libs.dfu)
}

tasks.test {
    useJUnitPlatform()
}
