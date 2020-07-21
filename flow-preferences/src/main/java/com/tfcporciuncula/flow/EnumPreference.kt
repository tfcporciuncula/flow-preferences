package com.tfcporciuncula.flow

import android.content.SharedPreferences
import kotlinx.coroutines.withContext
import java.lang.Enum.valueOf
import kotlin.coroutines.CoroutineContext

class EnumPreference<T : Enum<T>>(
  keyFlow: KeyFlow,
  private val sharedPreferences: SharedPreferences,
  private val key: String,
  private val enumClass: Class<T>,
  private val defaultValue: T,
  private val coroutineContext: CoroutineContext
) : BasePreference<T>(keyFlow, sharedPreferences, key, coroutineContext) {

  override fun get(): T = sharedPreferences.getString(key, null)?.let { valueOf(enumClass, it) } ?: defaultValue

  override fun set(value: T) = sharedPreferences.edit().putString(key, value.name).apply()

  override suspend fun setAndCommit(value: T) =
    withContext(coroutineContext) { sharedPreferences.edit().putString(key, value.name).commit() }
}
