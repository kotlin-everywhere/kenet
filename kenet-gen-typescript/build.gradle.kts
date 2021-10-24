plugins {
    kotlin("multiplatform")
    `maven-publish`
}

kotlin {
    jvm()

    sourceSets {
        @Suppress("UNUSED_VARIABLE")
        val jvmMain by getting {
            dependencies {
                api(project(":kenet-dsl"))
                implementation(kotlin("reflect"))
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}