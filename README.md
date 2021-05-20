[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.fredporciuncula/flow-preferences/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.fredporciuncula/flow-preferences)
[![Build Status](https://github.com/tfcporciuncula/flow-preferences/workflows/CI/badge.svg)](https://github.com/tfcporciuncula/flow-preferences/actions?query=workflow%3ACI)
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/)

# Flow Preferences

This is the [Kotlin Flow](https://kotlinlang.org/docs/reference/coroutines/flow.html) version of 
[rx-preferences](https://github.com/f2prateek/rx-preferences). It follows pretty much the same API and 
should feel familiar to anyone with experience there. But instead of RxJava, 
we have [Coroutines](https://kotlinlang.org/docs/reference/coroutines/coroutines-guide.html) -- mainly Flows.

## Download

```groovy
repositories {
  mavenCentral()
}

dependencies {
  implementation 'com.fredporciuncula:flow-preferences:1.5.0'
}
```

## How it looks like

Start with the regular `SharedPreferences`:

```kotlin
val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
```

Create an instance of `FlowSharedPreferences` from that:

```kotlin
val flowSharedPreferences = FlowSharedPreferences(sharedPreferences)
```

Get a preference:

```kotlin
val myPref = flowSharedPreferences.getInt("key", defaultValue = 10)
```

Go with the Flow:

```kotlin
myPref.asFlow().onEach { print(it) }.launchIn(scope)

```

## Extras

### Set and commit support

Preferences expose the regular `get()` and `put()` (named as `set()`) functions from `SharedPreferences`. 
But in addition to that, they also expose the `suspend` `setAndCommit()` function that puts the value and performs a
commit in case you must ensure the preference is persisted right away. There's also a `deleteAndCommit()`.

### Collector support

You can call `asCollector()` to ask a 
[`FlowCollector`](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow-collector/) 
from a preference. You can then persist values from a Flow directly to the preference:

```kotlin
val flow = flow { emit(1) }

scope.launch {
  myPref.asCollector().emitAll(flow)
}
```

You can use `asSyncCollector()` if you want to put **and commit** the value (like `setAndCommit()`) on each emission.

### Enum support

Enum classes work out of the box and are persisted as strings based on their `name` value (so make sure you `@Keep` them 
if you're using R8):

```kotlin
enum class MyEnum { A, B, C }

val myPref = flowSharedPreferences.getEnum("key", defaultValue = MyEnum.A)
```

### Object support

Arbitrary objects are also supported as long as an instance of `ObjectPreference.Serializer` is provided:

```kotlin
class TestObject(val id: Int)

val serializer =
  object : ObjectPreference.Serializer<TestObject> {
    override fun deserialize(serialized: String) = TestObject(serialized.toInt())
    override fun serialize(value: TestObject) = value.id.toString()
  }

val myPref = flowSharedPreferences.getObject("key", serializer, defaultValue = TestObject(0))
```

### Explicit nullability support

By default, strings, objects, enums and sets can never be `null`, so consumers don't ever have to worry about 
`null` checks. If you want to support nullable values, you can explicitly opt in by asking for the 
nullable-friendly preference types:

```kotlin
val nullableStringPreference = flowSharedPreferences.getNullableString("key", defaultValue = null)
```

# ⁕
