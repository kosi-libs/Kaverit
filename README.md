Kodein-Type
===========

A light multiplatform Kotlin reflection API.

* `erased` generates a `TypeToken` that represents a type without its (optional) generic parameters, available at **compile-time** and **run-time**.
* `generic` generates a `TypeToken` that represents a type with its generic parameters, available at **compile-time** only.
* `erasedComp` generates a `TypeToken` that represents a type with its generic parameters, available at **run-time** only.

All TypeTokens are comparable between them:

```kotlin

erased<String>() == generic<String>()

erased<List<*>>() == generic<List<*>>()

generic<List<String>>() == erasedComp<List<String>>(List::class, String::class)
```
