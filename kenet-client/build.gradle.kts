plugins {
    kotlin("multiplatform")
    `maven-publish`
}

kotlin {
    jvm()

    sourceSets {
        @Suppress("UNUSED_VARIABLE")
        val commonMain by getting {
            dependencies {
                api(project(":kenet-dsl"))
                val ktor_version = "1.6.0"
                implementation("io.ktor:ktor-client-core:$ktor_version")
                implementation("io.ktor:ktor-client-serialization:$ktor_version")
                implementation("io.ktor:ktor-client-cio:$ktor_version")
                implementation("io.ktor:ktor-client-logging:$ktor_version")
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val jvmMain by getting {
            dependencies {
                api(project(":kenet-dsl"))
                implementation("ch.qos.logback:logback-classic:1.2.3")
            }
        }
    }
}