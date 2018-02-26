import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    var kotlin_version: String = "1.2.21"
    var catalyst_version: String = "1.2.1"

    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(kotlinModule("gradle-plugin", kotlin_version))
    }
}

subprojects {

    group = "pt.um.tf.lab0"
    version = "1.0-SNAPSHOT"

    apply {
        plugin("kotlin")
        plugin("application")
    }

    var kotlin_version: String = "1.2.21"
    var catalyst_version: String = "1.2.1"

    repositories {
        mavenCentral()
    }

    dependencies {
        compile(kotlinModule("stdlib-jdk8", kotlin_version))
        compile("io.atomix.catalyst", "catalyst-buffer", catalyst_version)
        compile("io.atomix.catalyst", "catalyst-concurrent", catalyst_version)
        compile("io.atomix.catalyst", "catalyst-local", catalyst_version)
        compile("io.atomix.catalyst", "catalyst-netty", catalyst_version)
        compile("io.atomix.catalyst", "catalyst-serializer", catalyst_version)
        compile("io.atomix.catalyst", "catalyst-transport", catalyst_version)
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}

project(":lab0-srv") {
    dependencies {
        compile(project(":lab0-mes"))
    }
}

project(":lab0-cli") {
    dependencies {
        compile(project(":lab0-mes"))
    }
}
