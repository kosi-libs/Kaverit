plugins {
    id("org.kodein.root")
}

buildscript {
    repositories {
        maven(url = "https://kotlin.bintray.com/kotlinx")
    }
}

allprojects {
    group = "org.kodein.type"
    version = "0.1.0"
}

kodeinPublications {
    repo = "Kodein-Type"
}
