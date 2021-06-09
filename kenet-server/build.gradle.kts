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
                implementation("io.ktor:ktor-server-cio:$ktor_version")
                implementation("io.ktor:ktor-serialization:$ktor_version")
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val jvmMain by getting {
            dependencies {
                api(project(":kenet-dsl"))
                implementation("ch.qos.logback:logback-classic:1.2.3")
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}