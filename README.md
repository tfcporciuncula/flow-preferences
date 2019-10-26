[![Build Status](https://app.bitrise.io/app/83cbb59f8537b54e/status.svg?token=71wk-H1uKmybn_KB1Q7Gmw)](https://app.bitrise.io/app/83cbb59f8537b54e)
[![Release](https://jitpack.io/v/tfcporciuncula/flow-preferences.svg)](https://jitpack.io/#tfcporciuncula/flow-preferences)

# Flow Preferences

This is the [Kotlin Flow](https://kotlinlang.org/docs/reference/coroutines/flow.html) version of 
[rx-preferences](https://github.com/f2prateek/rx-preferences). It follows pretty much the same API and 
should feel familiar to anyone with experience there. But instead of RxJava, 
we have [Coroutines](https://kotlinlang.org/docs/reference/coroutines/coroutines-guide.html) -- mainly Flows.

## Download

```groovy
repositories {
  maven { url "https://jitpack.io" }
}

dependencies {
  implementation 'com.github.tfcporciuncula:flow-preferences:1.0.0'
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
scope.launch {
  myPref.asFlow().collect { print(it) }
}
```

## Extras

### Set and commit support

Preferences expose the regular `get()` and `put()` (named as `set()`) functions from `SharedPreferences`. 
But in addition to that, they also expose the `suspend` `setAndCommit()` function that puts the value and performs a
commit in case you must ensure the preference is persisted right away.

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

Enum classes work out of the box and are persisted as strings based on their `name` value:

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

# ⁕
