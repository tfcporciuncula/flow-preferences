package com.tfcporciuncula.tests

import com.google.common.truth.Truth.assertThat
import com.tfcporciuncula.flow.NullableSerializer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test

@ExperimentalCoroutinesApi
class NullableObjectPreferenceTest : BaseTest() {

  private class TestObject(val id: Int)

  private val serializer =
    object : NullableSerializer<TestObject?> {
      override fun deserialize(serialized: String?) = serialized?.let { TestObject(it.toInt()) }
      override fun serialize(value: TestObject?) = value?.id?.toString()
    }

  @Test fun testDefaultValues() {
    val preference1 = flowSharedPreferences.getNullableObject("key", serializer, defaultValue = TestObject(0))
    assertThat(preference1.get()!!.id).isEqualTo(0)

    val preference2 = flowSharedPreferences.getNullableObject("key", serializer, defaultValue = null)
    assertThat(preference2.get()).isNull()
  }

  @Test fun testSettingValues() {
    val preference = flowSharedPreferences.getNullableObject("key", serializer, defaultValue = TestObject(-1))

    preference.set(TestObject(100))
    assertThat(preference.get()!!.id).isEqualTo(100)

    runBlocking {
      preference.setAndCommit(TestObject(2000))
      assertThat(preference.get()!!.id).isEqualTo(2000)
    }
  }

  @Test fun testSettingNullValues() {
    val preference = flowSharedPreferences.getNullableObject("key", serializer, defaultValue = TestObject(-1))

    preference.set(null)
    assertThat(preference.get()!!.id).isEqualTo(-1)
    assertThat(preference.isSet()).isFalse()

    runBlocking {
      preference.setAndCommit(null)
      assertThat(preference.get()!!.id).isEqualTo(-1)
      assertThat(preference.isSet()).isFalse()
    }
  }
}
