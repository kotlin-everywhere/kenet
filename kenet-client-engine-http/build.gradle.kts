plugins {
    kotlin("multiplatform")
    `maven-publish`
}

kotlin {
    jvm()
    js(BOTH) {
        browser()
    }

    val ktor_version = "1.6.0"
    sourceSets {
        @Suppress("UNUSED_VARIABLE")
        val commonMain by getting {
            dependencies {
                api(project(":kenet-client"))
                implementation("io.ktor:ktor-client-core:$ktor_version")
                implementation("io.ktor:ktor-client-serialization:$ktor_version")
                implementation("io.ktor:ktor-client-logging:$ktor_version")
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-cio:$ktor_version")
                implementation("org.slf4j:slf4j-simple:1.7.25")
            }
        }
    }
}