package com.tfcporciuncula.flow

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

interface Preference<T> {

  fun get(): T

  fun set(value: T)

  suspend fun setAndCommit(value: T): Boolean

  fun isSet(): Boolean

  fun isNotSet(): Boolean

  fun delete()

  suspend fun deleteAndCommit(): Boolean

  fun asFlow(): Flow<T>

  fun asCollector(): FlowCollector<T>

  fun asSyncCollector(throwOnFailure: Boolean = false): FlowCollector<T>


  abstract class Base<T>(
    private val keyFlow: Flow<String>,
    private val sharedPreferences: SharedPreferences,
    private val key: String
  ) : Preference<T> {

    override fun isSet() = sharedPreferences.contains(key)

    override fun isNotSet() = !sharedPreferences.contains(key)

    override fun delete() = sharedPreferences.edit().remove(key).apply()

    override suspend fun deleteAndCommit() =
      withContext(Dispatchers.IO) { sharedPreferences.edit().remove(key).commit() }

    @ExperimentalCoroutinesApi override fun asFlow() =
      keyFlow
        .filter { it == key }
        .onStart { emit("first load trigger") }
        .map { get() }
        .buffer(Channel.CONFLATED)

    override fun asCollector() =
      object : FlowCollector<T> {
        override suspend fun emit(value: T) = set(value)
      }

    override fun asSyncCollector(throwOnFailure: Boolean) =
      object : FlowCollector<T> {
        override suspend fun emit(value: T) {
          if (!setAndCommit(value) && throwOnFailure) {
            throw ValueNotPersistedException("Value [$value] failed to be written to persistent storage.")
          }
        }
      }
  }

  class ValueNotPersistedException(message: String) : RuntimeException(message)
}
