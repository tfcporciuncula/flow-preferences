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

abstract class Preference<T>(
  private val keyFlow: Flow<String>,
  private val sharedPreferences: SharedPreferences,
  private val key: String,
  private val context: CoroutineContext
) {

  private class ValueNotPersistedException(message: String) : RuntimeException(message)

  abstract fun get(): T

  abstract fun set(value: T)

  abstract suspend fun setAndCommit(value: T): Boolean

  fun isSet() = sharedPreferences.contains(key)

  fun isNotSet() = !sharedPreferences.contains(key)

  fun delete() = sharedPreferences.edit().remove(key).apply()

  suspend fun deleteAndCommit() =
    withContext(context) { sharedPreferences.edit().remove(key).commit() }

  @ExperimentalCoroutinesApi fun asFlow() =
    keyFlow
      .filter { it == key }
      .onStart { emit("first load trigger") }
      .map { get() }
      .conflate()

  fun asCollector() =
    object : FlowCollector<T> {
      override suspend fun emit(value: T) = set(value)
    }

  fun asSyncCollector(throwOnFailure: Boolean = false) =
    object : FlowCollector<T> {
      override suspend fun emit(value: T) {
        if (!setAndCommit(value) && throwOnFailure) {
          throw ValueNotPersistedException("Value [$value] for key [$key] failed to be written to persistent storage.")
        }
      }
    }
}
