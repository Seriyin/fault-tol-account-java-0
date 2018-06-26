apply {
    plugin("application")
}

dependencies {
    implementation(kotlin("stdlib", extra["kotlin_version"] as String))
    implementation("io.atomix.catalyst", "catalyst-buffer", extra["catalyst_version"] as String)
    implementation("io.atomix.catalyst", "catalyst-concurrent", extra["catalyst_version"] as String)
    implementation("io.atomix.catalyst", "catalyst-local", extra["catalyst_version"] as String)
    implementation("io.atomix.catalyst", "catalyst-netty", extra["catalyst_version"] as String)
    implementation("io.atomix.catalyst", "catalyst-serializer", extra["catalyst_version"] as String)
    implementation("io.atomix.catalyst", "catalyst-transport", extra["catalyst_version"] as String)
    implementation("org.slf4j", "slf4j-api", extra["slf4j_version"] as String)
    implementation("io.github.microutils", "kotlin-logging", extra["kotlinlog_version"] as String)
    runtime("org.slf4j", "slf4j-simple", extra["slf4j_version"] as String)
    implementation(project(":lab0-mes"))
}