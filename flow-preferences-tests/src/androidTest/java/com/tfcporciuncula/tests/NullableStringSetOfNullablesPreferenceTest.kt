package com.tfcporciuncula.tests

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test

@ExperimentalCoroutinesApi
class NullableStringSetOfNullablesPreferenceTest : BaseTest() {

  @Test fun testDefaultValues() {
    val preference1 = flowSharedPreferences.getNullableStringSetOfNullables("key", defaultValue = setOf("a", "b"))
    assertThat(preference1.get()).isEqualTo(setOf("a", "b"))

    val preference2 = flowSharedPreferences.getNullableStringSetOfNullables("key", defaultValue = setOf("x", null, "a"))
    assertThat(preference2.get()).isEqualTo(setOf("x", null, "a"))

    val preference3 = flowSharedPreferences.getNullableStringSetOfNullables("key", defaultValue = null)
    assertThat(preference3.get()).isNull()
  }

  @Test fun testSettingValues() {
    val preference = flowSharedPreferences.getNullableStringSetOfNullables("key")

    preference.set(setOf("bla", null, "bla"))
    assertThat(preference.get()).isEqualTo(setOf("bla", null))

    runBlocking {
      preference.setAndCommit(emptySet())
      assertThat(preference.get()).isEqualTo(emptySet<String>())
    }
  }

  @Test fun testSettingNullValues() {
    val preference = flowSharedPreferences.getNullableStringSetOfNullables("key", defaultValue = emptySet())

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
