plugins {
    id("org.kodein.mpp-with-android")
}

kodein {
    kotlin {

        common.main.dependencies {
            implementation(projects.kaverit)
        }

        add(kodeinTargets.jvm.jvm)

        add(kodeinTargets.jvm.android) {
            test.dependencies {
                implementation(kodeinGlobals.kotlin.test.junit)
            }
        }

        add(kodeinTargets.native.all)

        add(kodeinTargets.js.js)

    }
}
