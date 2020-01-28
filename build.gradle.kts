plugins {
    id("org.kodein.root")
}

buildscript {
    repositories {
        maven(url = "https://kotlin.bintray.com/kotlinx")
    }
    dependencies {
        classpath("org.jetbrains.kotlinx:atomicfu-gradle-plugin:0.14.1-1.3.70-eap-42-2")
    }
}

allprojects {
    group = "org.kodein.memory"
    version = "0.1.0"
}

kodeinPublications {
    repo = "Kodein-Type"
}
