plugins {
    id("org.kodein.library.mpp")
}

kodein {
    kotlin {

        val allNonJvm = sourceSets.create("allNonJvmMain") {
            dependsOn(common.main)
        }

        add(kodeinTargets.jvm.jvm) {
            target.setCompileClasspath()
        }

        add(kodeinTargets.native.all) {
            main.dependsOn(allNonJvm)
        }

        add(kodeinTargets.js.js) {
            main.dependsOn(allNonJvm)
        }
    }
}

kodeinUpload {
    name = "Kaverit"
    description = "Kodein Type System"
}
