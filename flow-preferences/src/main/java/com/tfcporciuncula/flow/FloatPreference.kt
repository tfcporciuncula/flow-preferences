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
  private val context: CoroutineContext
) : Preference<Float>(keyFlow, sharedPreferences, key, context) {

  override fun get() = sharedPreferences.getFloat(key, defaultValue)

  override fun set(value: Float) = sharedPreferences.edit().putFloat(key, value).apply()

  override suspend fun setAndCommit(value: Float) =
    withContext(context) { sharedPreferences.edit().putFloat(key, value).commit() }
}
