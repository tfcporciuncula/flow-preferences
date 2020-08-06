package com.tfcporciuncula.flow

import android.content.SharedPreferences
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class NullableStringPreference(
  override val key: String,
  override val defaultValue: String?,
  keyFlow: KeyFlow,
  private val sharedPreferences: SharedPreferences,
  private val coroutineContext: CoroutineContext
) : BasePreference<String?>(key, keyFlow, sharedPreferences, coroutineContext) {

  override fun get() = sharedPreferences.getString(key, defaultValue)

  override fun set(value: String?) = sharedPreferences.edit().putString(key, value).apply()

  override suspend fun setAndCommit(value: String?) = withContext(coroutineContext) {
    sharedPreferences.edit().putString(key, value).commit()
  }
}
