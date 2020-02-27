package com.tfcporciuncula.flow

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class FlowSharedPreferences @JvmOverloads constructor(
  val sharedPreferences: SharedPreferences,
  val coroutineContext: CoroutineContext = Dispatchers.IO
) {

  val keyFlow = callbackFlow {
    val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key -> offer(key) }
    sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    awaitClose { sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener) }
  }

  fun getInt(key: String, defaultValue: Int = 0): Preference<Int> =
    IntPreference(keyFlow, sharedPreferences, key, defaultValue, coroutineContext)

  fun getLong(key: String, defaultValue: Long = 0): Preference<Long> =
    LongPreference(keyFlow, sharedPreferences, key, defaultValue, coroutineContext)

  fun getFloat(key: String, defaultValue: Float = 0f): Preference<Float> =
    FloatPreference(keyFlow, sharedPreferences, key, defaultValue, coroutineContext)

  fun getBoolean(key: String, defaultValue: Boolean = false): Preference<Boolean> =
    BooleanPreference(keyFlow, sharedPreferences, key, defaultValue, coroutineContext)

  fun getString(key: String, defaultValue: String = ""): Preference<String> =
    StringPreference(keyFlow, sharedPreferences, key, defaultValue, coroutineContext)

  fun getStringSet(key: String, defaultValue: Set<String> = emptySet()): Preference<Set<String>> =
    StringSetPreference(keyFlow, sharedPreferences, key, defaultValue, coroutineContext)

  fun getNullableStringSet(key: String, defaultValue: Set<String?> = emptySet()): Preference<Set<String?>> =
    NullableStringSetPreference(keyFlow, sharedPreferences, key, defaultValue, coroutineContext)

  fun <T> getObject(key: String, serializer: ObjectPreference.Serializer<T>, defaultValue: T): Preference<T> =
    ObjectPreference(keyFlow, sharedPreferences, key, serializer, defaultValue, coroutineContext)

  inline fun <reified T : Enum<T>> getEnum(key: String, defaultValue: T): Preference<T> =
    EnumPreference(keyFlow, sharedPreferences, key, T::class.java, defaultValue, coroutineContext)

  fun clear() = sharedPreferences.edit().clear().apply()
}
