package com.tfcporciuncula.flow

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.lang.Enum.valueOf
import kotlin.coroutines.CoroutineContext

class EnumPreference<T : Enum<T>>(
  keyFlow: Flow<String>,
  private val sharedPreferences: SharedPreferences,
  private val key: String,
  private val enumClass: Class<T>,
  private val defaultValue: T,
  private val context: CoroutineContext
) : Preference<T>(keyFlow, sharedPreferences, key, context) {

  override fun get(): T = sharedPreferences.getString(key, null)?.let { valueOf(enumClass, it) } ?: defaultValue

  override fun set(value: T) = sharedPreferences.edit().putString(key, value.name).apply()

  override suspend fun setAndCommit(value: T) =
    withContext(context) { sharedPreferences.edit().putString(key, value.name).commit() }
}
