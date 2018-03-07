subprojects {

    group = "pt.um.tf.lab0"
    version = "1.0-SNAPSHOT"

    apply {
        plugin("java")
        plugin("application")
    }

    // var vertx_version: String = "3.5.1"
    val catalyst_version: String = "1.2.1"

    repositories {
        mavenCentral()
        jcenter()
    }

    dependencies {
/*
        compile("io.vertx", "vertx-lang-kotlin", vertx_version)
        compile("io.vertx", "vertx-lang-kotlin-coroutines", vertx_version)
        compile("io.vertx", "vertx-core", vertx_version)
*/
        compile("io.atomix.catalyst", "catalyst-buffer", catalyst_version)
        compile("io.atomix.catalyst", "catalyst-concurrent", catalyst_version)
        compile("io.atomix.catalyst", "catalyst-local", catalyst_version)
        compile("io.atomix.catalyst", "catalyst-netty", catalyst_version)
        compile("io.atomix.catalyst", "catalyst-serializer", catalyst_version)
        compile("io.atomix.catalyst", "catalyst-transport", catalyst_version)
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
