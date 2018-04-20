val kotlin_version = "1.2.40"
val catalyst_version = "1.2.1"


dependencies {
    implementation(kotlinModule("stdlib-jdk8", kotlin_version))
    compile("io.atomix.catalyst", "catalyst-serializer", catalyst_version)
    compile("io.atomix.catalyst", "catalyst-transport", catalyst_version)
    compile(kotlinModule("stdlib-jdk8", kotlin_version))
}
