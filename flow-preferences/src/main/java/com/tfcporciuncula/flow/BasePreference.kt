package com.tfcporciuncula.flow

import android.content.SharedPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

abstract class BasePreference<T>(
  private val keyFlow: Flow<String?>,
  private val sharedPreferences: SharedPreferences,
  private val key: String,
  private val coroutineContext: CoroutineContext
) : Preference<T> {

  private class ValueNotPersistedException(message: String) : RuntimeException(message)

  override fun isSet() = sharedPreferences.contains(key)

  override fun isNotSet() = !sharedPreferences.contains(key)

  override fun delete() = sharedPreferences.edit().remove(key).apply()

  override suspend fun deleteAndCommit() =
    withContext(coroutineContext) { sharedPreferences.edit().remove(key).commit() }

  @ExperimentalCoroutinesApi
  override fun asFlow() =
    keyFlow
      .filter { it == key || it == null } // null means preferences were cleared (Android R+ exclusive behavior)
      .onStart { emit("first load trigger") }
      .map { get() }
      .conflate()

  override fun asCollector() =
    object : FlowCollector<T> {
      override suspend fun emit(value: T) = set(value)
    }

  override fun asSyncCollector(throwOnFailure: Boolean) =
    object : FlowCollector<T> {
      override suspend fun emit(value: T) {
        if (!setAndCommit(value) && throwOnFailure) {
          throw ValueNotPersistedException("Value [$value] for key [$key] failed to be written to persistent storage.")
        }
      }
    }
}
