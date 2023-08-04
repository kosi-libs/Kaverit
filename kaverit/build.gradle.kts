plugins {
    kodein.library.mpp
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

kodeinUpload {
    name = "Kaverit"
    description = "Kodein Type System"
}
