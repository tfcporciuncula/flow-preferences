package com.tfcporciuncula.tests

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test

@ExperimentalCoroutinesApi
class NullableStringSetPreferenceTest : BaseTest() {

  @Test fun testDefaultValues() {
    val preference1 = flowSharedPreferences.getNullableStringSet("key", defaultValue = setOf("a, b"))
    assertThat(preference1.get()).isEqualTo(setOf("a, b"))

    val preference2 = flowSharedPreferences.getNullableStringSet("key", defaultValue = setOf("x", null, "a"))
    assertThat(preference2.get()).isEqualTo(setOf("x", null, "a"))
  }

  @Test fun testSettingValues() {
    val preference = flowSharedPreferences.getNullableStringSet("key")

    preference.set(setOf("bla", null, "bla"))
    assertThat(preference.get()).isEqualTo(setOf("bla", null))

    runBlocking {
      preference.setAndCommit(emptySet())
      assertThat(preference.get()).isEqualTo(emptySet<String>())
    }
  }
}
