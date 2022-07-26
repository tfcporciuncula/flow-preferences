package com.fredporciuncula.flow.preferences.tests

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class StringSetOfNullablesPreferenceTest : BaseTest() {

  @Test fun testDefaultValues() {
    val preference1 = flowSharedPreferences.getStringSetOfNullables("key", defaultValue = setOf("a", "b"))
    assertThat(preference1.get()).isEqualTo(setOf("a", "b"))

    val preference2 = flowSharedPreferences.getStringSetOfNullables("key", defaultValue = setOf("x", null, "a"))
    assertThat(preference2.get()).isEqualTo(setOf("x", null, "a"))
  }

  @Test fun testSettingValues() = runTest {
    val preference = flowSharedPreferences.getStringSetOfNullables("key")

    preference.set(setOf("bla", null, "bla"))
    assertThat(preference.get()).isEqualTo(setOf("bla", null))

    preference.setAndCommit(emptySet())
    assertThat(preference.get()).isEqualTo(emptySet<String>())
  }
}
