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
    version = "1.1.0"
}

kodeinPublications {
    repo = "Kodein-Type"
}
