plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "packed"
include("example")
include("packed-core")
include("packed-ui")
include("ui-example")
include("packed-negative-spaces")
