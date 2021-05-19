package com.fredporciuncula.flow.preferences.tests

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test

@ExperimentalCoroutinesApi
class EnumPreferenceTest : BaseTest() {

  enum class TestEnum { A, B, C }

  @Test fun testDefaultValues() {
    val preference1 = flowSharedPreferences.getEnum("key1", defaultValue = TestEnum.A)
    assertThat(preference1.get()).isEqualTo(TestEnum.A)

    val preference2 = flowSharedPreferences.getEnum("key2", defaultValue = TestEnum.C)
    assertThat(preference2.get()).isEqualTo(TestEnum.C)
  }

  @Test fun testSettingValues() {
    val preference = flowSharedPreferences.getEnum("key", defaultValue = TestEnum.A)

    preference.set(TestEnum.B)
    assertThat(preference.get()).isEqualTo(TestEnum.B)

    runBlocking {
      preference.setAndCommit(TestEnum.C)
      assertThat(preference.get()).isEqualTo(TestEnum.C)
    }
  }
}
