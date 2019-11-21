package com.tfcporciuncula.flow

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class NullableStringSetPreference(
  keyFlow: Flow<String>,
  private val sharedPreferences: SharedPreferences,
  private val key: String,
  private val defaultValue: Set<String?>
) : Preference<Set<String?>>(keyFlow, sharedPreferences, key) {

  override fun get(): Set<String?> = sharedPreferences.getStringSet(key, defaultValue)!!

  override fun set(value: Set<String?>) = sharedPreferences.edit().putStringSet(key, value).apply()

  override suspend fun setAndCommit(value: Set<String?>) =
    withContext(Dispatchers.IO) { sharedPreferences.edit().putStringSet(key, value).commit() }
}
