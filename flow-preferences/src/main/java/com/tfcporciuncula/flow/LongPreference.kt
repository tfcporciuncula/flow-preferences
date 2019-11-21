package com.tfcporciuncula.flow

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LongPreference(
  keyFlow: Flow<String>,
  private val sharedPreferences: SharedPreferences,
  private val key: String,
  private val defaultValue: Long
) : Preference<Long>(keyFlow, sharedPreferences, key) {

  override fun get() = sharedPreferences.getLong(key, defaultValue)

  override fun set(value: Long) = sharedPreferences.edit().putLong(key, value).apply()

  override suspend fun setAndCommit(value: Long) =
    withContext(Dispatchers.IO) { sharedPreferences.edit().putLong(key, value).commit() }
}
