plugins {
    id("org.kodein.library.mpp")
}

kodein {
    kotlin {

        add(kodeinTargets.jvm.jvm) {
            target.setCompileClasspath()
        }

        add(kodeinTargets.native.all)

        add(kodeinTargets.js.js)

        sourceSets.all {
            languageSettings.useExperimentalAnnotation("kotlin.Experimental")
        }
    }
}
