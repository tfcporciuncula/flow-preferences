package com.tfcporciuncula.flow.tests

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test

@ExperimentalCoroutinesApi
class NullableStringPreferenceTest : BaseTest() {

  @Test fun testDefaultValues() {
    val preference1 = flowSharedPreferences.getNullableString("key", defaultValue = "abc")
    assertThat(preference1.get()).isEqualTo("abc")

    val preference2 = flowSharedPreferences.getNullableString("key", defaultValue = null)
    assertThat(preference2.get()).isNull()
  }

  @Test fun testSettingValues() {
    val preference = flowSharedPreferences.getNullableString("key")

    preference.set("--")
    assertThat(preference.get()).isEqualTo("--")

    runBlocking {
      preference.setAndCommit("x")
      assertThat(preference.get()).isEqualTo("x")
    }
  }

  @Test fun testSettingNullValues() {
    val preference = flowSharedPreferences.getNullableString("key", defaultValue = "default")

    preference.set(null)
    assertThat(preference.get()).isEqualTo("default")
    assertThat(preference.isSet()).isFalse()

    runBlocking {
      preference.setAndCommit(null)
      assertThat(preference.get()).isEqualTo("default")
      assertThat(preference.isSet()).isFalse()
    }
  }
}
