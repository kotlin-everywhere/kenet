plugins {
    kotlin("multiplatform") version "1.5.30" apply false
    kotlin("jvm") version "1.5.10" apply false
    kotlin("plugin.serialization") version "1.5.30" apply false
    id("com.github.johnrengelman.shadow") version "7.0.0" apply false
    `maven-publish`
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    tasks {
        withType<Test> {
            maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1
        }
    }
}