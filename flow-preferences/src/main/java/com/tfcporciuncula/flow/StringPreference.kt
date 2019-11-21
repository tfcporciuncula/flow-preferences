package com.tfcporciuncula.flow

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class StringPreference(
  keyFlow: Flow<String>,
  private val sharedPreferences: SharedPreferences,
  private val key: String,
  private val defaultValue: String
) : Preference<String>(keyFlow, sharedPreferences, key) {

  override fun get() = sharedPreferences.getString(key, defaultValue)!!

  override fun set(value: String) = sharedPreferences.edit().putString(key, value).apply()

  override suspend fun setAndCommit(value: String) =
    withContext(Dispatchers.IO) { sharedPreferences.edit().putString(key, value).commit() }
}
