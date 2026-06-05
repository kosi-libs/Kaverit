plugins {
    kodein.library.mppWithAndroid
}

kotlin.kodein {
    all()
    jvm {
        target.setCompileClasspath()
    }
    android {
        target.namespace = "org.kodein.type"
        target.optimization {
            consumerKeepRules.files("proguard-rules.pro")
            consumerKeepRules.publish = true
        }
    }
}

kotlin.sourceSets {
    val nonJvmMain = create("nonJvmMain") { dependsOn(commonMain.get()) }
    getByName("nativeMain").dependsOn(nonJvmMain)
    getByName("jsBasedMain").dependsOn(nonJvmMain)
}

kodeinUpload {
    name = "Kaverit"
    description = "Kodein Type System"
}
