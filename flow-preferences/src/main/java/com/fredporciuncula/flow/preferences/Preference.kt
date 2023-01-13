package com.fredporciuncula.flow.preferences

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

/**
 * A preference of type [T]. Instances can be created from [FlowSharedPreferences].
 */
interface Preference<T> {

  /**
   * The key for which this preference will store and retrieve values.
   */
  val key: String

  /**
   * The value returns as a fallback if none is stored.
   */
  val defaultValue: T

  /**
   * Retrieves the current stored value for this preference. Returns [defaultValue] if no value is set.
   */
  fun get(): T

  /**
   * Updates this preference's stored value to [value].
   */
  fun set(value: T)

  /**
   * Same as [set], but the update is immediately committed and written out to persistent storage synchronously.
   */
  suspend fun setAndCommit(value: T): Boolean

  /**
   * Returns true if this preference has a stored value, false otherwise.
   */
  fun isSet(): Boolean

  /**
   * Returns true if this preference has no stored value, false otherwise.
   */
  fun isNotSet(): Boolean

  /**
   * Deletes the stored value for this preference, if any.
   */
  fun delete()

  /**
   * Same as [delete], but the deletion is immediately committed and written out
   * to persistent storage synchronously.
   * @return true if a preference was successfully deleted from storage, false otherwise
   */
  suspend fun deleteAndCommit(): Boolean

  /**
   * Returns a conflated [Flow] that immediately emits the current stored value
   * (or default value, if no value is stored) and keeps emitting on any change.
   */
  fun asFlow(): Flow<T>

  /**
   * Returns a [FlowCollector] that can be used to set values from a [Flow].
   */
  fun asCollector(): FlowCollector<T>

  /**
   * Same as [asCollector], but the update is immediately committed and written out
   * to persistent storage synchronously on each emission.
   *
   * @param throwOnFailure if an exception should be thrown when committing fails.
   */
  fun asSyncCollector(throwOnFailure: Boolean = false): FlowCollector<T>
}
