plugins {
    kodein.mppWithAndroid
}

kotlin.kodein {
    all()

    common.mainDependencies {
        implementation(projects.kaverit)
    }
}
