plugins {
    kodein.mppWithAndroid
}

kotlin.kodein {
    all()
    common.mainDependencies {
        implementation(projects.kaverit)
    }
}

android {
    namespace = "org.kodein.di.type.test"
}