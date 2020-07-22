package com.tfcporciuncula.flow.tests

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test

@ExperimentalCoroutinesApi
class NullableEnumPreferenceTest : BaseTest() {

  enum class TestEnum { A, B, C }

  @Test fun testDefaultValues() {
    val preference1 = flowSharedPreferences.getNullableEnum("key1", defaultValue = TestEnum.A)
    assertThat(preference1.get()).isEqualTo(TestEnum.A)

    val preference2 = flowSharedPreferences.getNullableEnum<TestEnum>("key2", defaultValue = null)
    assertThat(preference2.get()).isNull()
  }

  @Test fun testSettingValues() {
    val preference = flowSharedPreferences.getNullableEnum("key", defaultValue = TestEnum.A)

    preference.set(TestEnum.B)
    assertThat(preference.get()).isEqualTo(TestEnum.B)

    runBlocking {
      preference.setAndCommit(TestEnum.C)
      assertThat(preference.get()).isEqualTo(TestEnum.C)
    }
  }

  @Test fun testSettingNullValues() {
    val preference = flowSharedPreferences.getNullableEnum("key", defaultValue = TestEnum.A)

    preference.set(null)
    assertThat(preference.get()).isEqualTo(TestEnum.A)
    assertThat(preference.isSet()).isFalse()

    runBlocking {
      preference.setAndCommit(null)
      assertThat(preference.get()).isEqualTo(TestEnum.A)
      assertThat(preference.isSet()).isFalse()
    }
  }
}
