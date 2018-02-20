import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    var kotlin_version: String by extra
    var catalyst_version: String by extra
    kotlin_version = "1.2.21"
    catalyst_version = "1.2.1"

    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(kotlinModule("gradle-plugin", kotlin_version))
    }
}

group = "pt.um.tf.lab0"
version = "1.0-SNAPSHOT"

apply {
    plugin("kotlin")
}

val kotlin_version: String by extra
val catalyst_version: String by extra

repositories {
    mavenCentral()
}

dependencies {
    compile(kotlinModule("stdlib-jdk8", kotlin_version))
    compile("io.atomix.catalyst", "catalyst-buffer", catalyst_version)
    compile("io.atomix.catalyst", "catalyst-common", catalyst_version)
    compile("io.atomix.catalyst", "catalyst-concurrent", catalyst_version)
    compile("io.atomix.catalyst", "catalyst-local", catalyst_version)
    compile("io.atomix.catalyst", "catalyst-netty", catalyst_version)
    compile("io.atomix.catalyst", "catalyst-serializer", catalyst_version)
    compile("io.atomix.catalyst", "catalyst-transport", catalyst_version)
    compile("io.atomix.catalyst", "catalyst-parent", catalyst_version)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}