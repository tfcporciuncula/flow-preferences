package com.fredporciuncula.flow.preferences

import android.content.SharedPreferences

/** Represents an abstraction for serializing custom data types to store them in [SharedPreferences]. */
interface Serializer<T : Any> {
  fun deserialize(serialized: String): T
  fun serialize(value: T): String
}

/** Represents an abstraction for serializing custom nullable data types to store them in [SharedPreferences]. */
interface NullableSerializer<T> {
  fun deserialize(serialized: String?): T?
  fun serialize(value: T?): String?
}

/** Allows for the simple case where the class [T] is serialized to string using the [toString] method override. */
fun interface StringSerializer<T : Any> : Serializer<T> {
  override fun serialize(value: T): String = value.toString()
}

/**
 * Allows for the simple case where the nullable class [T] is serialized to string using the [toString] method override.
 */
fun interface NullableStringSerializer<T : Any> : NullableSerializer<T> {
  override fun serialize(value: T?): String? = value?.toString()
}
