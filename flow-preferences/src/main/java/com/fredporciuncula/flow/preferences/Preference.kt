package com.fredporciuncula.flow.preferences

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

/**
 * Wrapper that complements [SharedPreferences] api more powerful and handful to use coroutines and kotlin [Flow]
 */
interface Preference<T> {

  /**
   * Represents the key, that is used by [SharedPreferences] to store a preference
   */
  val key: String

  /**
   * Represents a value, that will be returned, if the value by specific [key] will not be represented in [SharedPreferences] storage
   */
  val defaultValue: T

  /**
   * Typical method to synchronously get a value of type [T] from [SharedPreferences]
   */
  fun get(): T

  /**
   * Typical method to synchronously set a value of type [T] in [SharedPreferences]
   */
  fun set(value: T)

  /**
   * Asynchronous that sets a value of type [T] from [SharedPreferences]
   * @return true if the new values were successfully written
   * to persistent storage
   */
  suspend fun setAndCommit(value: T): Boolean

  /**
   * Checks whether the preferences contains a preference with a [key]
   *
   * @return true if the preference exists in the preferences,
   *         otherwise false
   */
  fun isSet(): Boolean

  /**
   * Checks whether the preferences not contains a preference with a [key]
   *
   * @return false if the preference exists in the preferences,
   *         otherwise true
   */
  fun isNotSet(): Boolean

  /**
   * Synchronously deletes a preference that is stored with a [key]
   */
  fun delete()

  /**
   * Asynchronously deletes a preference that is stored with a [key]
   * @return true if a preference was successfully deleted from storage, false otherwise
   */
  suspend fun deleteAndCommit(): Boolean

  /**
   * Represents a [SharedPreferences] storage as a data source
   * @return [Flow] of type [T] that will emit preference values by a given [key]
   */
  fun asFlow(): Flow<T>

  fun asCollector(): FlowCollector<T>

  fun asSyncCollector(throwOnFailure: Boolean = false): FlowCollector<T>
}
