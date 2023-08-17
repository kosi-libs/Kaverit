plugins {
    kodein.library.mppWithAndroid
}

kotlin.kodein {
    all()
    jvm {
        target.setCompileClasspath()
    }
}

kotlin.sourceSets {
    val nonJvmMain = create("nonJvmMain") { dependsOn(commonMain.get()) }
    getByName("nativeMain").dependsOn(nonJvmMain)
    getByName("jsBasedMain").dependsOn(nonJvmMain)
    getByName("androidMain").dependsOn(getByName("jvmMain"))
}

android {
    namespace = "org.kodein.type"
    defaultConfig {
        consumerProguardFiles("proguard-rules.pro")
    }
}

kodeinUpload {
    name = "Kaverit"
    description = "Kodein Type System"
}
