package com.fredporciuncula.flow.preferences

import android.content.SharedPreferences
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

val SharedPreferences.keyFlow
  get() = callbackFlow {
    // key can be null when preferences are cleared on API Level 30+
    val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key: String? -> trySend(key) }
    registerOnSharedPreferenceChangeListener(listener)
    awaitClose { unregisterOnSharedPreferenceChangeListener(listener) }
  }
