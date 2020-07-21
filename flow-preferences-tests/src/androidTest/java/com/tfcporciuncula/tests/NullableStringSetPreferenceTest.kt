package com.tfcporciuncula.tests

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test

@ExperimentalCoroutinesApi
class NullableStringSetPreferenceTest : BaseTest() {

  @Test fun testDefaultValues() {
    val preference1 = flowSharedPreferences.getNullableStringSet("key", defaultValue = setOf("a", "b"))
    assertThat(preference1.get()).isEqualTo(setOf("a", "b"))

    val preference2 = flowSharedPreferences.getNullableStringSet("key", defaultValue = null)
    assertThat(preference2.get()).isNull()
  }

  @Test fun testSettingValues() {
    val preference = flowSharedPreferences.getNullableStringSet("key")

    preference.set(setOf("bla", "bla"))
    assertThat(preference.get()).isEqualTo(setOf("bla"))

    runBlocking {
      preference.setAndCommit(emptySet())
      assertThat(preference.get()).isEqualTo(emptySet<String>())
    }
  }

  @Test fun testSettingNullValues() {
    val preference = flowSharedPreferences.getNullableStringSet("key", defaultValue = emptySet())

    preference.set(null)
    assertThat(preference.get()).isEqualTo(emptySet<String>())
    assertThat(preference.isSet()).isFalse()

    runBlocking {
      preference.setAndCommit(null)
      assertThat(preference.get()).isEqualTo(emptySet<String>())
      assertThat(preference.isSet()).isFalse()
    }
  }
}
