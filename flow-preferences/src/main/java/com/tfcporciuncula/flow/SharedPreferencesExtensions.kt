package com.tfcporciuncula.flow

import android.content.SharedPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
val SharedPreferences.keyFlow
  get() = callbackFlow {
    // key can be null when preferences are cleared on Android R+
    val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key: String? -> offerCatching(key) }
    registerOnSharedPreferenceChangeListener(listener)
    awaitClose { unregisterOnSharedPreferenceChangeListener(listener) }
  }

// https://github.com/Kotlin/kotlinx.coroutines/issues/974
private fun <E> SendChannel<E>.offerCatching(element: E): Boolean {
  return runCatching { offer(element) }.getOrDefault(false)
}
