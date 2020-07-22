package com.tfcporciuncula.flow

import android.content.SharedPreferences
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class StringSetOfNullablesPreference(
  keyFlow: KeyFlow,
  private val sharedPreferences: SharedPreferences,
  private val key: String,
  private val defaultValue: Set<String?>,
  private val coroutineContext: CoroutineContext
) : BasePreference<Set<String?>>(keyFlow, sharedPreferences, key, coroutineContext) {

  override fun get(): Set<String?> = sharedPreferences.getStringSet(key, defaultValue)!!

  override fun set(value: Set<String?>) = sharedPreferences.edit().putStringSet(key, value).apply()

  override suspend fun setAndCommit(value: Set<String?>) = withContext(coroutineContext) {
    sharedPreferences.edit().putStringSet(key, value).commit()
  }
}
