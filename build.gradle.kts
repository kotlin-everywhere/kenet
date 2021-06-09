plugins {
    kotlin("multiplatform") version "1.5.10" apply false
    kotlin("plugin.serialization") version "1.5.0" apply false
    `maven-publish`
}

allprojects {
    repositories {
        mavenCentral()
    }
}