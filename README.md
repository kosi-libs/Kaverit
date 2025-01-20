Kaverit
===========

A light multiplatform Kotlin reflection API.

* `erased` generates a [`TypeToken`](https://github.com/kosi-libs/Kaverit/blob/master/kaverit/src/commonMain/kotlin/org/kodein/type/TypeToken.kt) that represents a type without its (optional) generic parameters, constructed at **compile-time** (no run-time reflection overhead).
* `generic` generates a [`TypeToken`](https://github.com/kosi-libs/Kaverit/blob/master/kaverit/src/commonMain/kotlin/org/kodein/type/TypeToken.kt) that represents a type with its generic parameters, constructed at **compile-time** (with reflection).
* `erasedComp` generates a [`TypeToken`](https://github.com/kosi-libs/Kaverit/blob/master/kaverit/src/commonMain/kotlin/org/kodein/type/TypeToken.kt) that represents a type with its generic parameters, constructed at **run-time** (with explicit parameters: no run-time overhead but verbose syntax).

All TypeTokens are comparable between them:

```kotlin
erased<String>() == generic<String>()

erased<List<*>>() == generic<List<*>>()

generic<List<String>>() == erasedComp<List<String>>(List::class, String::class)
```

## Supported by

[![JetBrains logo.](https://resources.jetbrains.com/storage/products/company/brand/logos/jetbrains.svg)](https://jb.gg/OpenSourceSupport)