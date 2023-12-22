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
