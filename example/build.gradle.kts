dependencies {
  api(project(":packed-core"))
  api(project(":packed-negative-spaces"))
  api(libs.kotlin.test)
  api(libs.kgit)
  api(libs.slf4j.log4j)

  runtimeClasspath(libs.dfu)
}
