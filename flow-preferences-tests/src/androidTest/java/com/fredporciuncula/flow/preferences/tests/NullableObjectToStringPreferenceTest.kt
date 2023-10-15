package com.fredporciuncula.flow.preferences.tests

import com.fredporciuncula.flow.preferences.NullableStringSerializer
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test

@ExperimentalCoroutinesApi
class NullableObjectToStringPreferenceTest : BaseTest() {

  private data class TestObject(val int: Int, val string: String) {
    override fun toString(): String = "$int,$string"
  }

  private fun parseTestObject(string: String?): TestObject? {
    string ?: return null
    val split = string.split(",")
    return TestObject(
      int = split[0].toInt(),
      string = split[1],
    )
  }

  private val serializer = NullableStringSerializer(::parseTestObject)

  @Test fun testDefaultValues() {
    val default = TestObject(int = 123, string = "abc")
    val preference = flowSharedPreferences.getNullableObject("key", serializer, default)
    assertThat(preference.get()).isEqualTo(default)
  }

  @Test fun testSettingValues() {
    val default = TestObject(int = 123, string = "abc")
    val preference = flowSharedPreferences.getNullableObject(key = "key", serializer, default)

    val newValue = TestObject(int = 456, string = "def")
    preference.set(newValue)
    assertThat(preference.get()).isEqualTo(newValue)
  }

  @Test fun testSettingNullValues() {
    val default = TestObject(int = 123, string = "abc")
    val preference = flowSharedPreferences.getNullableObject("key", serializer, default)

    preference.set(null)
    assertThat(preference.get()).isEqualTo(default)
    assertThat(preference.isSet()).isFalse()

    runBlocking {
      preference.setAndCommit(null)
      assertThat(preference.get()).isEqualTo(default)
      assertThat(preference.isSet()).isFalse()
    }
  }
}
