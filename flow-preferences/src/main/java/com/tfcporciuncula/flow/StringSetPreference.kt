package com.tfcporciuncula.flow

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class StringSetPreference(
  keyFlow: Flow<String>,
  private val sharedPreferences: SharedPreferences,
  private val key: String,
  private val defaultValue: Set<String>,
  private val context: CoroutineContext
) : Preference<Set<String>>(keyFlow, sharedPreferences, key, context) {

  override fun get(): Set<String> = sharedPreferences.getStringSet(key, defaultValue)!!

  override fun set(value: Set<String>) = sharedPreferences.edit().putStringSet(key, value).apply()

  override suspend fun setAndCommit(value: Set<String>) =
    withContext(context) { sharedPreferences.edit().putStringSet(key, value).commit() }
}
