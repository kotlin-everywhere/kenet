plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
    application
}

kotlin {
    sourceSets {
        @Suppress("UNUSED_VARIABLE")
        val main by getting {
            dependencies {
                implementation(project(":benchmark:common"))
                implementation(project(":kenet-gen-typescript"))
            }
        }
    }
}


application {
    mainClass.set("MainKt")
}