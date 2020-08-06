package com.tfcporciuncula.flow

import android.content.SharedPreferences
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class NullableObjectPreference<T>(
  override val key: String,
  private val serializer: NullableSerializer<T>,
  override val defaultValue: T?,
  keyFlow: KeyFlow,
  private val sharedPreferences: SharedPreferences,
  private val coroutineContext: CoroutineContext
) : BasePreference<T?>(key, keyFlow, sharedPreferences, coroutineContext) {

  override fun get() =
    sharedPreferences.getString(key, null)?.let { serializer.deserialize(it) } ?: defaultValue

  override fun set(value: T?) =
    sharedPreferences.edit().putString(key, serializer.serialize(value)).apply()

  override suspend fun setAndCommit(value: T?) = withContext(coroutineContext) {
    sharedPreferences.edit().putString(key, serializer.serialize(value)).commit()
  }
}
