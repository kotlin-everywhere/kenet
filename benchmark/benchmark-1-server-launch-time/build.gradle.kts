plugins {
    kotlin("jvm")
    application
}

kotlin {
    sourceSets {
        @Suppress("UNUSED_VARIABLE")
        val main by getting {
            dependencies {
                implementation(project(":benchmark:common"))
                implementation(project(":kenet-server-engine-http"))
            }
        }
    }
}


application {
    mainClass.set("MainKt")
    applicationDefaultJvmArgs = listOf("-Xmx512m")
}