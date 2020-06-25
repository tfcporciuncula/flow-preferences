package com.tfcporciuncula.flow

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class IntPreference(
  keyFlow: Flow<String?>,
  private val sharedPreferences: SharedPreferences,
  private val key: String,
  private val defaultValue: Int,
  private val coroutineContext: CoroutineContext
) : BasePreference<Int>(keyFlow, sharedPreferences, key, coroutineContext) {

  override fun get() = sharedPreferences.getInt(key, defaultValue)

  override fun set(value: Int) = sharedPreferences.edit().putInt(key, value).apply()

  override suspend fun setAndCommit(value: Int) =
    withContext(coroutineContext) { sharedPreferences.edit().putInt(key, value).commit() }
}
