package com.fredporciuncula.flow.preferences.tests

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class NullableStringSetPreferenceTest : BaseTest() {

  @Test fun testDefaultValues() {
    val preference1 = flowSharedPreferences.getNullableStringSet("key", defaultValue = setOf("a", "b"))
    assertThat(preference1.get()).isEqualTo(setOf("a", "b"))

    val preference2 = flowSharedPreferences.getNullableStringSet("key", defaultValue = null)
    assertThat(preference2.get()).isNull()
  }

  @Test fun testSettingValues() = runTest {
    val preference = flowSharedPreferences.getNullableStringSet("key")

    preference.set(setOf("bla", "bla"))
    assertThat(preference.get()).isEqualTo(setOf("bla"))

    preference.setAndCommit(emptySet())
    assertThat(preference.get()).isEqualTo(emptySet<String>())
  }

  @Test fun testSettingNullValues() = runTest {
    val preference = flowSharedPreferences.getNullableStringSet("key", defaultValue = emptySet())

    preference.set(null)
    assertThat(preference.get()).isEqualTo(emptySet<String>())
    assertThat(preference.isSet()).isFalse()

    preference.setAndCommit(null)
    assertThat(preference.get()).isEqualTo(emptySet<String>())
    assertThat(preference.isSet()).isFalse()
  }
}
