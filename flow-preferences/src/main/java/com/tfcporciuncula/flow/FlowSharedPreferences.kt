package com.tfcporciuncula.flow

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.CoroutineContext

internal typealias KeyFlow = Flow<String?>

@ExperimentalCoroutinesApi
class FlowSharedPreferences @JvmOverloads constructor(
  val sharedPreferences: SharedPreferences,
  val coroutineContext: CoroutineContext = Dispatchers.IO
) {

  internal val keyFlow: KeyFlow = callbackFlow {
    val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key -> offer(key) }
    sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    awaitClose { sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener) }
  }

  @JvmOverloads
  fun getInt(key: String, defaultValue: Int = 0): Preference<Int> =
    IntPreference(keyFlow, sharedPreferences, key, defaultValue, coroutineContext)

  @JvmOverloads
  fun getLong(key: String, defaultValue: Long = 0): Preference<Long> =
    LongPreference(keyFlow, sharedPreferences, key, defaultValue, coroutineContext)

  @JvmOverloads
  fun getFloat(key: String, defaultValue: Float = 0f): Preference<Float> =
    FloatPreference(keyFlow, sharedPreferences, key, defaultValue, coroutineContext)

  @JvmOverloads
  fun getBoolean(key: String, defaultValue: Boolean = false): Preference<Boolean> =
    BooleanPreference(keyFlow, sharedPreferences, key, defaultValue, coroutineContext)

  @JvmOverloads
  fun getString(key: String, defaultValue: String = ""): Preference<String> =
    StringPreference(keyFlow, sharedPreferences, key, defaultValue, coroutineContext)

  @JvmOverloads
  fun getNullableString(key: String, defaultValue: String? = null): Preference<String?> =
    NullableStringPreference(keyFlow, sharedPreferences, key, defaultValue, coroutineContext)

  @JvmOverloads
  fun getStringSet(key: String, defaultValue: Set<String> = emptySet()): Preference<Set<String>> =
    StringSetPreference(keyFlow, sharedPreferences, key, defaultValue, coroutineContext)

  @JvmOverloads
  fun getNullableStringSet(key: String, defaultValue: Set<String>? = null): Preference<Set<String>?> =
    NullableStringSetPreference(keyFlow, sharedPreferences, key, defaultValue, coroutineContext)

  @JvmOverloads
  fun getStringSetOfNullables(key: String, defaultValue: Set<String?> = emptySet()): Preference<Set<String?>> =
    StringSetOfNullablesPreference(keyFlow, sharedPreferences, key, defaultValue, coroutineContext)

  @JvmOverloads
  fun getNullableStringSetOfNullables(key: String, defaultValue: Set<String?>? = null): Preference<Set<String?>?> =
    NullableStringSetOfNullablesPreference(keyFlow, sharedPreferences, key, defaultValue, coroutineContext)

  fun <T : Any> getObject(
    key: String,
    serializer: Serializer<T>,
    defaultValue: T
  ): Preference<T> =
    ObjectPreference(keyFlow, sharedPreferences, key, serializer, defaultValue, coroutineContext)

  fun <T> getNullableObject(
    key: String,
    serializer: NullableSerializer<T>,
    defaultValue: T?
  ): Preference<T?> =
    NullableObjectPreference(keyFlow, sharedPreferences, key, serializer, defaultValue, coroutineContext)

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
