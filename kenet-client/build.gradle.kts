plugins {
    kotlin("multiplatform")
    `maven-publish`
    id("org.jetbrains.dokka")
}

kotlin {
    jvm()
    js(BOTH) {
        browser()
    }

    sourceSets {
        @Suppress("UNUSED_VARIABLE")
        val commonMain by getting {
            dependencies {
                api(project(":kenet-dsl"))
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val jvmMain by getting {
            dependencies {
                implementation("org.slf4j:slf4j-simple:1.7.25")
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