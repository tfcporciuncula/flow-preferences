package com.tfcporciuncula.flow

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class FloatPreference(
  keyFlow: Flow<String>,
  private val sharedPreferences: SharedPreferences,
  private val key: String,
  private val defaultValue: Float,
  private val coroutineContext: CoroutineContext
) : Preference<Float>(keyFlow, sharedPreferences, key, coroutineContext) {

  override fun get() = sharedPreferences.getFloat(key, defaultValue)

  override fun set(value: Float) = sharedPreferences.edit().putFloat(key, value).apply()

  override suspend fun setAndCommit(value: Float) =
    withContext(coroutineContext) { sharedPreferences.edit().putFloat(key, value).commit() }
}
