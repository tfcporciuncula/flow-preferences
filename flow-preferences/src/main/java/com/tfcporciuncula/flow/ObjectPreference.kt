package com.tfcporciuncula.flow

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class ObjectPreference<T>(
  keyFlow: Flow<String>,
  private val sharedPreferences: SharedPreferences,
  private val key: String,
  private val serializer: Serializer<T>,
  private val defaultValue: T,
  private val context: CoroutineContext
) : Preference<T>(keyFlow, sharedPreferences, key, context) {

  interface Serializer<T> {

    fun deserialize(serialized: String): T

    fun serialize(value: T): String
  }

  override fun get() =
    sharedPreferences.getString(key, null)?.let { serializer.deserialize(it) } ?: defaultValue

  override fun set(value: T) =
    sharedPreferences.edit().putString(key, serializer.serialize(value)).apply()

  override suspend fun setAndCommit(value: T) =
    withContext(context) { sharedPreferences.edit().putString(key, serializer.serialize(value)).commit() }
}
