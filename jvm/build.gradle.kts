plugins {
    application
}

allprojects {
    apply(plugin = "org.gradle.application")
    repositories {
        mavenCentral()
    }
}