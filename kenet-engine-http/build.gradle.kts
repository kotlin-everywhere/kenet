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
}