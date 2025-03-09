dependencies {
    testImplementation(libs.kotlin.test)
    api(libs.kgit)
    api(libs.zip)
    compileOnlyApi(libs.jetbrains.annotations)
    compileOnlyApi(libs.slf4j.api)
    compileOnlyApi(libs.slf4j.log4j)
    compileOnlyApi(libs.dfu)
}

tasks.test {
    useJUnitPlatform()
}
