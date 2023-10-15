package com.fredporciuncula.flow.preferences.tests

import com.fredporciuncula.flow.preferences.StringSerializer
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class ObjectToStringPreferenceTest : BaseTest() {

  private data class TestObject(val int: Int, val string: String) {
    override fun toString(): String = "$int,$string"
  }

  private fun parseTestObject(string: String): TestObject {
    val split = string.split(",")
    return TestObject(
      int = split[0].toInt(),
      string = split[1],
    )
  }

  private val serializer = StringSerializer(::parseTestObject)

  @Test fun testDefaultValues() {
    val default = TestObject(int = 123, string = "abc")
    val preference = flowSharedPreferences.getObject("key", serializer, default)
    assertThat(preference.get()).isEqualTo(default)
  }

  @Test fun testSettingValues() {
    val default = TestObject(int = 123, string = "abc")
    val preference = flowSharedPreferences.getObject(key = "key", serializer, default)

    val newValue = TestObject(int = 456, string = "def")
    preference.set(newValue)
    assertThat(preference.get()).isEqualTo(newValue)
  }
}
