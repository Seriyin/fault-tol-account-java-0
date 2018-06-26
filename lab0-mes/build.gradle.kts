dependencies {
    implementation(kotlin("stdlib", extra["kotlin_version"] as String))
    implementation("io.atomix.catalyst", "catalyst-serializer", extra["catalyst_version"] as String)
    implementation("io.atomix.catalyst", "catalyst-transport", extra["catalyst_version"] as String)
}
