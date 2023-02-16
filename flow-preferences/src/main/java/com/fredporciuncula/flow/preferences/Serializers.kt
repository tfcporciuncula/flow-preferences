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
