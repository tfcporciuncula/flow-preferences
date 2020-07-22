package com.tfcporciuncula.flow

import android.content.SharedPreferences
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class NullableStringPreference(
  keyFlow: KeyFlow,
  private val sharedPreferences: SharedPreferences,
  private val key: String,
  private val defaultValue: String?,
  private val coroutineContext: CoroutineContext
) : BasePreference<String?>(keyFlow, sharedPreferences, key, coroutineContext) {

  override fun get() = sharedPreferences.getString(key, defaultValue)

  override fun set(value: String?) = sharedPreferences.edit().putString(key, value).apply()

  override suspend fun setAndCommit(value: String?) = withContext(coroutineContext) {
    sharedPreferences.edit().putString(key, value).commit()
  }
}
