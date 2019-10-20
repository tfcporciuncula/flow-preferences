package com.tfcporciuncula.tests

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test

@ExperimentalCoroutinesApi
class StringPreferenceTest : BaseTest() {

  @Test fun testDefaultValues() {
    val preference1 = flowSharedPreferences.getString("key", defaultValue = "abc")
    assertThat(preference1.get()).isEqualTo("abc")

    val preference2 = flowSharedPreferences.getString("key", defaultValue = "@#$")
    assertThat(preference2.get()).isEqualTo("@#$")
  }

  @Test fun testSettingValues() {
    val preference = flowSharedPreferences.getString("key")

    preference.set("--")
    assertThat(preference.get()).isEqualTo("--")

    runBlocking {
      preference.setAndCommit("x")
      assertThat(preference.get()).isEqualTo("x")
    }
  }
}
