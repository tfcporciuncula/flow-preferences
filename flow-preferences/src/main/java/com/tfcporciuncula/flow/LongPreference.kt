package com.tfcporciuncula.flow

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class LongPreference(
  keyFlow: Flow<String>,
  private val sharedPreferences: SharedPreferences,
  private val key: String,
  private val defaultValue: Long,
  private val context: CoroutineContext
) : Preference<Long>(keyFlow, sharedPreferences, key, context) {

  override fun get() = sharedPreferences.getLong(key, defaultValue)

  override fun set(value: Long) = sharedPreferences.edit().putLong(key, value).apply()

  override suspend fun setAndCommit(value: Long) =
    withContext(context) { sharedPreferences.edit().putLong(key, value).commit() }
}
