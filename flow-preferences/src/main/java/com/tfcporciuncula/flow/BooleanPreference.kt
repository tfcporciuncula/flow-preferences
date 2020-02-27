package com.tfcporciuncula.flow

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class BooleanPreference(
  keyFlow: Flow<String>,
  private val sharedPreferences: SharedPreferences,
  private val key: String,
  private val defaultValue: Boolean,
  private val coroutineContext: CoroutineContext
) : Preference<Boolean>(keyFlow, sharedPreferences, key, coroutineContext) {

  override fun get() = sharedPreferences.getBoolean(key, defaultValue)

  override fun set(value: Boolean) = sharedPreferences.edit().putBoolean(key, value).apply()

  override suspend fun setAndCommit(value: Boolean) =
    withContext(coroutineContext) { sharedPreferences.edit().putBoolean(key, value).commit() }
}
