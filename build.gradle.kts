plugins {
    `maven-publish`
}

subprojects {
    apply(plugin = "maven-publish")

    repositories {
        mavenCentral()
    }
}