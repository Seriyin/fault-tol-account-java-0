import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    val kotlin_version = "1.2.50"

    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", kotlin_version))
    }
}

subprojects {
    group = "pt.um.tf.lab0"
    version = "1.0-SNAPSHOT"

    apply {
        plugin("kotlin")
    }

    extra["kotlin_version"] = "1.2.50"
    extra["catalyst_version"] = "1.2.1"
    extra.set("slf4j_version", "1.8.0-beta2")
    extra.set("kotlinlog_version", "1.5.4")


    repositories {
        mavenCentral()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}