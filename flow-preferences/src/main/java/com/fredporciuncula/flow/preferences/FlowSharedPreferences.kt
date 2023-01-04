package com.fredporciuncula.flow.preferences

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

internal typealias KeyFlow = Flow<String?>

class FlowSharedPreferences @JvmOverloads constructor(
  val sharedPreferences: SharedPreferences,
  val coroutineContext: CoroutineContext = EmptyCoroutineContext,
) {

  private val keyFlow: KeyFlow = sharedPreferences.keyFlow

  @JvmOverloads
  fun getInt(key: String, defaultValue: Int = 0): Preference<Int> =
    IntPreference(key, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  @JvmOverloads
  fun getLong(key: String, defaultValue: Long = 0): Preference<Long> =
    LongPreference(key, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  @JvmOverloads
  fun getFloat(key: String, defaultValue: Float = 0f): Preference<Float> =
    FloatPreference(key, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  @JvmOverloads
  fun getBoolean(key: String, defaultValue: Boolean = false): Preference<Boolean> =
    BooleanPreference(key, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  @JvmOverloads
  fun getString(key: String, defaultValue: String = ""): Preference<String> =
    StringPreference(key, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  @JvmOverloads
  fun getNullableString(key: String, defaultValue: String? = null): Preference<String?> =
    NullableStringPreference(key, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  @JvmOverloads
  fun getStringSet(key: String, defaultValue: Set<String> = emptySet()): Preference<Set<String>> =
    StringSetPreference(key, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  @JvmOverloads
  fun getNullableStringSet(key: String, defaultValue: Set<String>? = null): Preference<Set<String>?> =
    NullableStringSetPreference(key, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  @JvmOverloads
  fun getStringSetOfNullables(key: String, defaultValue: Set<String?> = emptySet()): Preference<Set<String?>> =
    StringSetOfNullablesPreference(key, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  @JvmOverloads
  fun getNullableStringSetOfNullables(key: String, defaultValue: Set<String?>? = null): Preference<Set<String?>?> =
    NullableStringSetOfNullablesPreference(key, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  fun <T : Any> getObject(
    key: String,
    serializer: Serializer<T>,
    defaultValue: T
  ): Preference<T> =
    ObjectPreference(key, serializer, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  fun <T> getNullableObject(
    key: String,
    serializer: NullableSerializer<T>,
    defaultValue: T?
  ): Preference<T?> =
    NullableObjectPreference(key, serializer, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  inline fun <reified T : Enum<T>> getEnum(key: String, defaultValue: T): Preference<T> {
    val serializer = object : Serializer<T> {
      override fun deserialize(serialized: String) = enumValueOf<T>(serialized)
      override fun serialize(value: T) = value.name
    }
    return getObject(key, serializer, defaultValue)
  }

  inline fun <reified T : Enum<T>> getNullableEnum(key: String, defaultValue: T?): Preference<T?> {
    val serializer = object : NullableSerializer<T> {
      override fun deserialize(serialized: String?) = serialized?.let { enumValueOf<T>(serialized) }
      override fun serialize(value: T?) = value?.name
    }
    return getNullableObject(key, serializer, defaultValue)
  }

  fun clear() = sharedPreferences.edit().clear().apply()
}
