package com.fredporciuncula.flow.preferences.tests

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test

@ExperimentalCoroutinesApi
class PreferenceTest : BaseTest() {

  @Test fun testIsSet() {
    val preference = flowSharedPreferences.getBoolean("key", defaultValue = true)

    assertThat(preference.isSet()).isFalse()

    preference.set(true)

    assertThat(preference.isSet()).isTrue()
  }

  @Test fun testIsNotSet() {
    val preference = flowSharedPreferences.getBoolean("key", defaultValue = false)

    assertThat(preference.isNotSet()).isTrue()

    preference.set(true)

    assertThat(preference.isNotSet()).isFalse()
  }

  @Test fun testDelete() {
    val preference = flowSharedPreferences.getString("key", defaultValue = "")
    preference.set("some value")

    preference.delete()

    assertThat(preference.isSet()).isFalse()
    assertThat(preference.get()).isEmpty()
  }

  @Test fun testDeleteAndCommit() {
    val preference = flowSharedPreferences.getLong("key", defaultValue = 0)
    preference.set(340L)

    runBlocking {
      preference.deleteAndCommit()
    }

    assertThat(preference.isSet()).isFalse()
    assertThat(preference.get()).isEqualTo(0)
  }

  @Test fun testClear() {
    val preference1 = flowSharedPreferences.getInt("key1").apply { set(10) }
    val preference2 = flowSharedPreferences.getString("key2").apply { set("xyz") }

    flowSharedPreferences.clear()

    assertThat(preference1.isSet()).isFalse()
    assertThat(preference2.isSet()).isFalse()
  }

  @Test fun testMultipleFlows() {
    val preference = flowSharedPreferences.getLong("key", defaultValue = -1)

    runBlocking {
      assertThat(preference.asFlow().first()).isEqualTo(-1)

      preference.set(10)
      assertThat(preference.asFlow().first()).isEqualTo(10)

      preference.set(20)
      assertThat(preference.asFlow().first()).isEqualTo(20)
      assertThat(preference.asFlow().first()).isEqualTo(20)
      assertThat(preference.asFlow().first()).isEqualTo(20)

      preference.delete()
      assertThat(preference.asFlow().first()).isEqualTo(-1)
    }
  }

  @Test fun testFlowConflatedBehavior() {
    val preference = flowSharedPreferences.getFloat("key", defaultValue = 0.5f)

    preference.set(20f)
    preference.set(30f)
    preference.set(50f)

    runBlocking {
      assertThat(preference.asFlow().first()).isEqualTo(50f)
    }
  }

  @Test fun testCollector() {
    val preference = flowSharedPreferences.getInt("key", defaultValue = 1)

    runBlocking {
      preference.asCollector().emit(123)
      assertThat(preference.get()).isEqualTo(123)

      val flow = flow { emit(456) }
      preference.asCollector().emitAll(flow)
      assertThat(preference.get()).isEqualTo(456)
    }
  }

  @Test fun testSyncCollector() {
    val preference = flowSharedPreferences.getString("key", defaultValue = "sync")

    runBlocking {
      preference.asSyncCollector().emit("xyz")
      assertThat(preference.get()).isEqualTo("xyz")

      val flow = flow { emit("abc") }
      preference.asSyncCollector().emitAll(flow)
      assertThat(preference.get()).isEqualTo("abc")
    }
  }
}
