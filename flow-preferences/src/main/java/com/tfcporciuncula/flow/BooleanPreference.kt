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
  private val context: CoroutineContext
) : Preference<Boolean>(keyFlow, sharedPreferences, key, context) {

  override fun get() = sharedPreferences.getBoolean(key, defaultValue)

  override fun set(value: Boolean) = sharedPreferences.edit().putBoolean(key, value).apply()

  override suspend fun setAndCommit(value: Boolean) =
    withContext(context) { sharedPreferences.edit().putBoolean(key, value).commit() }
}
