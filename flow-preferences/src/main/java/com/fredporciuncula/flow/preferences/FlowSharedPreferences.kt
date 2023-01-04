package com.fredporciuncula.flow.preferences

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

internal typealias KeyFlow = Flow<String?>

/**
 * Specific wrapper, that presents wide range of methods for handful using of [SharedPreferences] via [Preference] wrapper
 * @param sharedPreferences takes an instance of [SharedPreferences]
 * @param coroutineContext takes a [CoroutineContext]. Can be used to override standard behavior, e.g. logging
 */
class FlowSharedPreferences @JvmOverloads constructor(
  val sharedPreferences: SharedPreferences,
  val coroutineContext: CoroutineContext = EmptyCoroutineContext,
) {

  private val keyFlow: KeyFlow = sharedPreferences.keyFlow

  /**
   * Creates a [Preference] wrapper for [Int] by a given key from [SharedPreferences]. By default, all operations will be executed on [Dispatchers.IO]
   * @param key key for a [SharedPreferences] storage value
   * @param defaultValue will be returned if value by given key is not found
   * @return [Preference] wrapper of type [Int]
   */
  @JvmOverloads
  fun getInt(key: String, defaultValue: Int = 0): Preference<Int> =
    IntPreference(key, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  /**
   * Creates a [Preference] wrapper for [Long] by a given key from [SharedPreferences]. By default, all operations will be executed on [Dispatchers.IO]
   * @param key key for a [SharedPreferences] storage value
   * @param defaultValue will be returned if value by given key is not found
   * @return [Preference] wrapper of type [Long]
   */
  @JvmOverloads
  fun getLong(key: String, defaultValue: Long = 0): Preference<Long> =
    LongPreference(key, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  /**
   * Creates a [Preference] wrapper for [Float] by a given key from [SharedPreferences]. By default, all operations will be executed on [Dispatchers.IO]
   * @param key key for a [SharedPreferences] storage value
   * @param defaultValue will be returned if value by given key is not found
   * @return [Preference] wrapper of type [Float]
   */
  @JvmOverloads
  fun getFloat(key: String, defaultValue: Float = 0f): Preference<Float> =
    FloatPreference(key, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  /**
   * Creates a [Preference] wrapper for [Boolean] by a given key from [SharedPreferences]. By default, all operations will be executed on [Dispatchers.IO]
   * @param key key for a [SharedPreferences] storage value
   * @param defaultValue will be returned if value by given key is not found
   * @return [Preference] wrapper of type [Boolean]
   */
  @JvmOverloads
  fun getBoolean(key: String, defaultValue: Boolean = false): Preference<Boolean> =
    BooleanPreference(key, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  /**
   * Creates a [Preference] wrapper for [String] by a given key from [SharedPreferences]. By default, all operations will be executed on [Dispatchers.IO]
   * @param key key for a [SharedPreferences] storage value
   * @param defaultValue will be returned if value by given key is not found
   * @return [Preference] wrapper of type [String]
   */
  @JvmOverloads
  fun getString(key: String, defaultValue: String = ""): Preference<String> =
    StringPreference(key, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  /**
   * Creates a [Preference] wrapper for nullable [String] by a given key from [SharedPreferences]. By default, all operations will be executed on [Dispatchers.IO]
   * @param key key for a [SharedPreferences] storage value
   * @param defaultValue will be returned if value by given key is not found
   * @return [Preference] wrapper of nullable [String]
   */
  @JvmOverloads
  fun getNullableString(key: String, defaultValue: String? = null): Preference<String?> =
    NullableStringPreference(key, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  /**
   * Creates a [Preference] wrapper for [Set] of [String] by a given key from [SharedPreferences]. By default, all operations will be executed on [Dispatchers.IO]
   * @param key key for a [SharedPreferences] storage value
   * @param defaultValue will be returned if value by given key is not found
   * @return [Preference] wrapper of [Set] of [String]
   */
  @JvmOverloads
  fun getStringSet(key: String, defaultValue: Set<String> = emptySet()): Preference<Set<String>> =
    StringSetPreference(key, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  /**
   * Creates a [Preference] wrapper for nullable [Set] of [String] by a given key from [SharedPreferences]. By default, all operations will be executed on [Dispatchers.IO]
   * @param key key for a [SharedPreferences] storage value
   * @param defaultValue will be returned if value by given key is not found
   * @return [Preference] wrapper of nullable [Set] of [String]
   */
  @JvmOverloads
  fun getNullableStringSet(key: String, defaultValue: Set<String>? = null): Preference<Set<String>?> =
    NullableStringSetPreference(key, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  /**
   * Creates a [Preference] wrapper for [Set] of nullable [String] by a given key from [SharedPreferences]. By default, all operations will be executed on [Dispatchers.IO]
   * @param key key for a [SharedPreferences] storage value
   * @param defaultValue will be returned if value by given key is not found
   * @return [Preference] wrapper of [Set] of nullable [String]
   */
  @JvmOverloads
  fun getStringSetOfNullables(key: String, defaultValue: Set<String?> = emptySet()): Preference<Set<String?>> =
    StringSetOfNullablesPreference(key, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  /**
   * Creates a [Preference] wrapper for nullable [Set] of nullable [String] by a given key from [SharedPreferences]. By default, all operations will be executed on [Dispatchers.IO]
   * @param key key for a [SharedPreferences] storage value
   * @param defaultValue will be returned if value by given key is not found
   * @return [Preference] wrapper of nullable [Set] of nullable [String]
   */
  @JvmOverloads
  fun getNullableStringSetOfNullables(key: String, defaultValue: Set<String?>? = null): Preference<Set<String?>?> =
    NullableStringSetOfNullablesPreference(key, defaultValue, keyFlow, sharedPreferences, Dispatchers.IO + coroutineContext)

  /**
   * Creates a [Preference] wrapper for [Any] by a given key from [SharedPreferences]. By default, all operations will be executed on [Dispatchers.IO]
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
   * Creates a [Preference] wrapper for nullable [Any] by a given key from [SharedPreferences]. By default, all operations will be executed on [Dispatchers.IO]
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
   * Creates a [Preference] wrapper for [Enum] subtype by a given key from [SharedPreferences]. By default, all operations will be executed on [Dispatchers.IO]
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
   * Creates a [Preference] wrapper for nullable [Enum] subtype by a given key from [SharedPreferences]. By default, all operations will be executed on [Dispatchers.IO]
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
   * Clears the [SharedPreferences] storage
   */
  fun clear() = sharedPreferences.edit().clear().apply()
}
