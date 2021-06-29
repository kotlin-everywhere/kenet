plugins {
    kotlin("jvm")
    application
}

kotlin {
    sourceSets {
        @Suppress("UNUSED_VARIABLE")
        val main by getting {
            dependencies {
                implementation(project(":test-echo:def"))
                implementation(project(":kenet-client-engine-http"))
            }
        }
    }
}


application {
    mainClass.set("test.echo.client.ClientJvmKt")
}