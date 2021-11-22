plugins {
    kotlin("multiplatform")
    `maven-publish`
    id("org.jetbrains.dokka")
}

kotlin {
    jvm()

    sourceSets {
        @Suppress("UNUSED_VARIABLE")
        val commonMain by getting {
            dependencies {
                api(project(":kenet-dsl"))
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