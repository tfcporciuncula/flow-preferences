package com.tfcporciuncula.flow

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class IntPreference(
  keyFlow: Flow<String>,
  private val sharedPreferences: SharedPreferences,
  private val key: String,
  private val defaultValue: Int
) : Preference.Base<Int>(keyFlow, sharedPreferences, key) {

  override fun get() = sharedPreferences.getInt(key, defaultValue)

  override fun set(value: Int) = sharedPreferences.edit().putInt(key, value).apply()

  override suspend fun setAndCommit(value: Int) =
    withContext(Dispatchers.IO) { sharedPreferences.edit().putInt(key, value).commit() }
}
