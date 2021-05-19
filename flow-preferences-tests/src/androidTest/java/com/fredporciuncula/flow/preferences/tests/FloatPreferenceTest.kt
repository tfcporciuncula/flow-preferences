package com.fredporciuncula.flow.preferences.tests

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test

@ExperimentalCoroutinesApi
class FloatPreferenceTest : BaseTest() {

  @Test fun testDefaultValues() {
    val preference1 = flowSharedPreferences.getFloat("key", defaultValue = 0f)
    assertThat(preference1.get()).isEqualTo(0f)

    val preference2 = flowSharedPreferences.getFloat("key", defaultValue = 1.1f)
    assertThat(preference2.get()).isEqualTo(1.1f)
  }

  @Test fun testSettingValues() {
    val preference = flowSharedPreferences.getFloat("key")

    preference.set(23f)
    assertThat(preference.get()).isEqualTo(23f)

    runBlocking {
      preference.setAndCommit(100f)
      assertThat(preference.get()).isEqualTo(100f)
    }
  }
}
