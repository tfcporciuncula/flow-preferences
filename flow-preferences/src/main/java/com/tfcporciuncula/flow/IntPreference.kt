package com.tfcporciuncula.flow

import android.content.SharedPreferences
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class IntPreference(
  override val key: String,
  override val defaultValue: Int,
  keyFlow: KeyFlow,
  private val sharedPreferences: SharedPreferences,
  private val coroutineContext: CoroutineContext
) : BasePreference<Int>(key, keyFlow, sharedPreferences, coroutineContext) {

  override fun get() = sharedPreferences.getInt(key, defaultValue)

  override fun set(value: Int) = sharedPreferences.edit().putInt(key, value).apply()

  override suspend fun setAndCommit(value: Int) = withContext(coroutineContext) {
    sharedPreferences.edit().putInt(key, value).commit()
  }
}
