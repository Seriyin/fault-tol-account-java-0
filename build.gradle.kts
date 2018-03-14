import com.sun.javafx.scene.CameraHelper.project
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    var kotlin_version: String = "1.2.21"
    //  var vertx_version: String = "3.5.1"
    var catalyst_version: String = "1.2.1"

    repositories {
        mavenCentral()
        jcenter()
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
    // var vertx_version: String = "3.5.1"
    var catalyst_version: String = "1.2.1"

    repositories {
        mavenCentral()
    }

    dependencies {
        compile(kotlinModule("stdlib-jdk8", kotlin_version))
/*
        compile("io.vertx", "vertx-lang-kotlin", vertx_version)
        compile("io.vertx", "vertx-lang-kotlin-coroutines", vertx_version)
        compile("io.vertx", "vertx-core", vertx_version)
*/
        compile("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "0.22.3")
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
        compile(project(":lab0-srv"))
    }
}
