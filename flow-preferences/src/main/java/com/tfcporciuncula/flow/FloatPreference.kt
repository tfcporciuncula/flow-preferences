package com.tfcporciuncula.flow

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class FloatPreference(
  keyFlow: Flow<String>,
  private val sharedPreferences: SharedPreferences,
  private val key: String,
  private val defaultValue: Float
) : Preference<Float>(keyFlow, sharedPreferences, key) {

  override fun get() = sharedPreferences.getFloat(key, defaultValue)

  override fun set(value: Float) = sharedPreferences.edit().putFloat(key, value).apply()

  override suspend fun setAndCommit(value: Float) =
    withContext(Dispatchers.IO) { sharedPreferences.edit().putFloat(key, value).commit() }
}
