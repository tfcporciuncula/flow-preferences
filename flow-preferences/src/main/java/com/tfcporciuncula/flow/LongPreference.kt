package com.tfcporciuncula.flow

import android.content.SharedPreferences
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class LongPreference(
  keyFlow: KeyFlow,
  private val sharedPreferences: SharedPreferences,
  private val key: String,
  private val defaultValue: Long,
  private val coroutineContext: CoroutineContext
) : BasePreference<Long>(keyFlow, sharedPreferences, key, coroutineContext) {

  override fun get() = sharedPreferences.getLong(key, defaultValue)

  override fun set(value: Long) = sharedPreferences.edit().putLong(key, value).apply()

  override suspend fun setAndCommit(value: Long) = withContext(coroutineContext) {
    sharedPreferences.edit().putLong(key, value).commit()
  }
}
