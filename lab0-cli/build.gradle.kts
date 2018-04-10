val kotlin_version = "1.2.31"
val catalyst_version = "1.2.1"

dependencies {
    implementation(kotlinModule("stdlib-jdk8", kotlin_version))
    compile("io.atomix.catalyst", "catalyst-buffer", catalyst_version)
    compile("io.atomix.catalyst", "catalyst-concurrent", catalyst_version)
    compile("io.atomix.catalyst", "catalyst-local", catalyst_version)
    compile("io.atomix.catalyst", "catalyst-netty", catalyst_version)
    compile("io.atomix.catalyst", "catalyst-serializer", catalyst_version)
    compile("io.atomix.catalyst", "catalyst-transport", catalyst_version)
    compile(project(":lab0-mes"))
}