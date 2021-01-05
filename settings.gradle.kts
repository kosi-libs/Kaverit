import org.kodein.internal.gradle.settings.android

buildscript {
    repositories {
        mavenLocal()
        maven(url = "https://dl.bintray.com/kodein-framework/kodein-dev")
        maven(url = "https://dl.bintray.com/kodein-framework/Kodein-Internal-Gradle")
    }
    dependencies {
        classpath("org.kodein.internal.gradle:kodein-internal-gradle-settings:5.3.0-kotlin-1.4.30-M1-84")
    }
}

apply { plugin("org.kodein.settings") }

rootProject.name = "Kodein-Type"

include(
    "kodein-type",
    "tests"
)
