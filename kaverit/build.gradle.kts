plugins {
    id("org.kodein.library.mpp-with-android")
}

kodeinAndroid {
    android {
        defaultConfig {
            consumerProguardFiles("proguard-rules.pro")
        }
    }
}

kodein {
    kotlin {

        val allNonJvm = sourceSets.create("allNonJvmMain") {
            dependsOn(common.main)
        }

        add(kodeinTargets.jvm.jvm) {
            target.setCompileClasspath()
        }

        add(kodeinTargets.jvm.android) {
            main.dependsOn(sourceSets["jvmMain"])
        }

        add(kodeinTargets.native.all) {
            main.dependsOn(allNonJvm)
        }

        add(kodeinTargets.js.ir.js) {
            main.dependsOn(allNonJvm)
        }

        sourceSets.all {
            languageSettings {
                optIn("kotlin.RequiresOptIn")
            }
        }
    }
}

kodeinUpload {
    name = "Kodein-Type"
    description = "Kodein Type System"
}
