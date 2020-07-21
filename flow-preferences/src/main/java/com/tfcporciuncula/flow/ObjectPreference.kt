package com.tfcporciuncula.flow

import android.content.SharedPreferences
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class ObjectPreference<T>(
  keyFlow: KeyFlow,
  private val sharedPreferences: SharedPreferences,
  private val key: String,
  private val serializer: Serializer<T>,
  private val defaultValue: T,
  private val coroutineContext: CoroutineContext
) : BasePreference<T>(keyFlow, sharedPreferences, key, coroutineContext) {

  interface Serializer<T> {

    fun deserialize(serialized: String): T

    fun serialize(value: T): String
  }

  override fun get() =
    sharedPreferences.getString(key, null)?.let { serializer.deserialize(it) } ?: defaultValue

  override fun set(value: T) =
    sharedPreferences.edit().putString(key, serializer.serialize(value)).apply()

  override suspend fun setAndCommit(value: T) =
    withContext(coroutineContext) { sharedPreferences.edit().putString(key, serializer.serialize(value)).commit() }
}
