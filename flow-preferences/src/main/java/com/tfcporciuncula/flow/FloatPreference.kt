package com.tfcporciuncula.flow

import android.content.SharedPreferences
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class FloatPreference(
  keyFlow: KeyFlow,
  private val sharedPreferences: SharedPreferences,
  private val key: String,
  private val defaultValue: Float,
  private val coroutineContext: CoroutineContext
) : BasePreference<Float>(keyFlow, sharedPreferences, key, coroutineContext) {

  override fun get() = sharedPreferences.getFloat(key, defaultValue)

  override fun set(value: Float) = sharedPreferences.edit().putFloat(key, value).apply()

  override suspend fun setAndCommit(value: Float) = withContext(coroutineContext) {
    sharedPreferences.edit().putFloat(key, value).commit()
  }
}
