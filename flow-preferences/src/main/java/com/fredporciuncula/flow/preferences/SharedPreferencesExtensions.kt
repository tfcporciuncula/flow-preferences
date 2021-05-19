package com.fredporciuncula.flow.preferences

import android.content.SharedPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
val SharedPreferences.keyFlow
  get() = callbackFlow {
    // key can be null when preferences are cleared on Android R+
    val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key: String? -> trySend(key) }
    registerOnSharedPreferenceChangeListener(listener)
    awaitClose { unregisterOnSharedPreferenceChangeListener(listener) }
  }
