package com.tfcporciuncula.flow

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

abstract class Preference<T>(
  private val keyFlow: Flow<String>,
  private val sharedPreferences: SharedPreferences,
  private val key: String
) {

  private class ValueNotPersistedException(message: String) : RuntimeException(message)

  abstract fun get(): T

  abstract fun set(value: T)

  abstract suspend fun setAndCommit(value: T): Boolean

  fun isSet() = sharedPreferences.contains(key)

  fun isNotSet() = !sharedPreferences.contains(key)

  fun delete() = sharedPreferences.edit().remove(key).apply()

  suspend fun deleteAndCommit() =
    withContext(Dispatchers.IO) { sharedPreferences.edit().remove(key).commit() }

  @ExperimentalCoroutinesApi fun asFlow() =
    keyFlow
      .filter { it == key }
      .onStart { emit("first load trigger") }
      .map { get() }
      .buffer(Channel.CONFLATED)

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
