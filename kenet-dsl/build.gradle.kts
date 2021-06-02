plugins {
    kotlin("multiplatform") version "1.5.0"
    `maven-publish`
}

kotlin {
    jvm()
    js {
        browser()
        nodejs()
    }

    sourceSets {
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test")) // This brings all the platform dependencies automatically
            }
        }
    }
}