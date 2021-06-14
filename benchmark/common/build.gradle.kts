plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()
    
    sourceSets {
        @Suppress("UNUSED_VARIABLE")
        val commonMain by getting {
            dependencies {
                implementation(project(":kenet-dsl"))
            }
        }
    }
}
