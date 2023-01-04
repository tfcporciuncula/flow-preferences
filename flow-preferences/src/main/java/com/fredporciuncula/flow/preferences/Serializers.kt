package com.fredporciuncula.flow.preferences

import android.content.SharedPreferences

/**
 * Represents an abstraction for serializing custom data types to store them in [SharedPreferences]
 *
 * Note: For example can be used as a wrapper for json serializers like Gson, Moshi, etc.
 */
interface Serializer<T : Any> {
  fun deserialize(serialized: String): T
  fun serialize(value: T): String
}

/**
 * Represents an abstraction for serializing custom nullable data types to store them in [SharedPreferences]
 *
 * Note: For example can be used as a wrapper for json serializers like Gson, Moshi, etc.
 */
interface NullableSerializer<T> {
  fun deserialize(serialized: String?): T?
  fun serialize(value: T?): String?
}
