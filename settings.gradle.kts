import org.kodein.internal.gradle.settings.android

buildscript {
    repositories {
        mavenLocal()
        maven(url = "https://raw.githubusercontent.com/kosi-libs/kodein-internal-gradle-plugin/mvn-repo")
    }
    dependencies {
        classpath("org.kodein.internal.gradle:kodein-internal-gradle-settings:6.18.3")
    }
}

apply { plugin("org.kodein.settings") }

rootProject.name = "Kaverit"

include(
    "kaverit",
    "tests"
)
