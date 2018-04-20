apply {
    plugin("java-library")
}

val catalyst_version: String = "1.2.1"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    compile("io.atomix.catalyst", "catalyst-transport", catalyst_version)
}