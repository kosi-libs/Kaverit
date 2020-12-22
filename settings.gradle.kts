buildscript {
    repositories {
        mavenLocal()
        maven(url = "https://dl.bintray.com/kodein-framework/kodein-dev")
        maven(url = "https://dl.bintray.com/kodein-framework/Kodein-Internal-Gradle")
    }
    dependencies {
        classpath("org.kodein.internal.gradle:kodein-internal-gradle-settings:5.2.0")
    }
}

apply { plugin("org.kodein.settings") }

rootProject.name = "Kodein-Type"

include(
        "kodein-type",
        ""
)
