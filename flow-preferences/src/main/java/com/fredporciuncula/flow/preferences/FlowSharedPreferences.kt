package com.fredporciuncula.flow.preferences

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

internal typealias KeyFlow = Flow<String?>

/**
 * Factory for [Preference] objects based on an instance of [SharedPreferences]. Commit operations happen on coroutineContext, which is [Dispatchers.IO] by default.
 */
class FlowSharedPreferences @JvmOverloads constructor(
  val sharedPreferences: SharedPreferences,
  val coroutineContext: CoroutineContext = EmptyCoroutineContext,
) {

  private val keyFlow: KeyFlow = sharedPreferences.keyFlow

  /**
   * Creates a [Preference] of type [Int] with the given key and defaultValue.
   *
   * Note: By default, all operations will be executed on [Dispatchers.IO]
   */
  @JvmOverloads
  fun getInt(key: String, defaultValue: Int = 0): Preference<Int> =
    IntPreference(key, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  /**
   * Creates a [Preference] of type [Long] with the given key and defaultValue.
   *
   * Note: By default, all operations will be executed on [Dispatchers.IO]
   */
  @JvmOverloads
  fun getLong(key: String, defaultValue: Long = 0): Preference<Long> =
    LongPreference(key, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  /**
   *
   * Creates a [Preference] of type [Float] with the given key and defaultValue.
   *
   * Note: By default, all operations will be executed on [Dispatchers.IO]
   */
  @JvmOverloads
  fun getFloat(key: String, defaultValue: Float = 0f): Preference<Float> =
    FloatPreference(key, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  /**
   * Creates a [Preference] of type [Boolean] with the given key and defaultValue.
   *
   * Note: By default, all operations will be executed on [Dispatchers.IO]
   */
  @JvmOverloads
  fun getBoolean(key: String, defaultValue: Boolean = false): Preference<Boolean> =
    BooleanPreference(key, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  /**
   * Creates a [Preference] of type [String] with the given key and defaultValue.
   *
   * Note: By default, all operations will be executed on [Dispatchers.IO]
   */
  @JvmOverloads
  fun getString(key: String, defaultValue: String = ""): Preference<String> =
    StringPreference(key, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  /**
   * Creates a [Preference] of type nullable [String] with the given key and defaultValue.
   *
   * Note: By default, all operations will be executed on [Dispatchers.IO]
   */
  @JvmOverloads
  fun getNullableString(key: String, defaultValue: String? = null): Preference<String?> =
    NullableStringPreference(key, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  /**
   * Creates a [Preference] of type [Set] of [String] with the given key and defaultValue.
   *
   * Note: By default, all operations will be executed on [Dispatchers.IO]
   */
  @JvmOverloads
  fun getStringSet(key: String, defaultValue: Set<String> = emptySet()): Preference<Set<String>> =
    StringSetPreference(key, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  /**
   * Creates a [Preference] of type nullable [Set] of [String] with the given key and defaultValue.
   *
   * Note: By default, all operations will be executed on [Dispatchers.IO]
   */
  @JvmOverloads
  fun getNullableStringSet(key: String, defaultValue: Set<String>? = null): Preference<Set<String>?> =
    NullableStringSetPreference(key, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  /**
   * Creates a [Preference] of type [Set] of nullable [String] with the given key and defaultValue.
   *
   * Note: By default, all operations will be executed on [Dispatchers.IO]
   */
  @JvmOverloads
  fun getStringSetOfNullables(key: String, defaultValue: Set<String?> = emptySet()): Preference<Set<String?>> =
    StringSetOfNullablesPreference(key, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  /**
   * Creates a [Preference] of type nullable [Set] of nullable [String] with the given key and defaultValue.
   *
   * Note: By default, all operations will be executed on [Dispatchers.IO]
   */
  @JvmOverloads
  fun getNullableStringSetOfNullables(key: String, defaultValue: Set<String?>? = null): Preference<Set<String?>?> =
    NullableStringSetOfNullablesPreference(key, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  /**
   * Creates a [Preference] of type [T] with the given key and defaultValue. A [Serializer] of the same type must be passed so the value can be stored as a [String].
   *
   * Note: By default, all operations will be executed on [Dispatchers.IO]
   * @param key key for a [SharedPreferences] storage value
   * @param defaultValue will be returned if value by given key is not found
   * @return [Preference] wrapper of [Any] subtype
   */
  fun <T : Any> getObject(
    key: String,
    serializer: Serializer<T>,
    defaultValue: T
  ): Preference<T> =
    ObjectPreference(key, serializer, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  /**
   * Creates a [Preference] of type nullable [T] with the given key and defaultValue. A [Serializer] of the same type must be passed so the value can be stored as a [String].
   *
   * Note: By default, all operations will be executed on [Dispatchers.IO]
   * @param key key for a [SharedPreferences] storage value
   * @param defaultValue will be returned if value by given key is not found
   * @return [Preference] wrapper of nullable [Any] subtype
   */
  fun <T> getNullableObject(
    key: String,
    serializer: NullableSerializer<T>,
    defaultValue: T?
  ): Preference<T?> =
    NullableObjectPreference(key, serializer, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  /**
   * Creates a [Preference] wrapper for [Enum] subtype by a given key from [SharedPreferences].
   *
   * Note: By default, all operations will be executed on [Dispatchers.IO]
   * @param key key for a [SharedPreferences] storage value
   * @param defaultValue will be returned if value by given key is not found
   * @return [Preference] wrapper of [Enum] subtype
   */
  inline fun <reified T : Enum<T>> getEnum(key: String, defaultValue: T): Preference<T> {
    val serializer = object : Serializer<T> {
      override fun deserialize(serialized: String) = enumValueOf<T>(serialized)
      override fun serialize(value: T) = value.name
    }
    return getObject(key, serializer, defaultValue)
  }

  /**
   * Creates a [Preference] wrapper for nullable [Enum] subtype by a given key from [SharedPreferences].
   *
   * Note: By default, all operations will be executed on [Dispatchers.IO]
   * @param key key for a [SharedPreferences] storage value
   * @param defaultValue will be returned if value by given key is not found
   * @return [Preference] wrapper of nullable [Enum] subtype
   */
  inline fun <reified T : Enum<T>> getNullableEnum(key: String, defaultValue: T?): Preference<T?> {
    val serializer = object : NullableSerializer<T> {
      override fun deserialize(serialized: String?) = serialized?.let { enumValueOf<T>(serialized) }
      override fun serialize(value: T?) = value?.name
    }
    return getNullableObject(key, serializer, defaultValue)
  }

  /**
   * Clears the [SharedPreferences] storage.
   */
  fun clear() = sharedPreferences.edit().clear().apply()
}
