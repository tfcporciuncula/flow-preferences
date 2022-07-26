package com.fredporciuncula.flow.preferences.tests

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
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

  @Test fun testDeleteAndCommit() = runTest {
    val preference = flowSharedPreferences.getLong("key", defaultValue = 0)
    preference.set(340L)

    preference.deleteAndCommit()

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

  @Test fun testMultipleFlows() = runTest {
    val preference = flowSharedPreferences.getLong("key", defaultValue = -1)

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

  @Test fun testFlowConflatedBehavior() = runTest {
    val preference = flowSharedPreferences.getFloat("key", defaultValue = 0.5f)

    val results = mutableListOf<Float>()
    val deferred = CompletableDeferred<Unit>()

    val job = launch(UnconfinedTestDispatcher()) {
      preference.asFlow()
        .distinctUntilChanged() // see: https://github.com/tfcporciuncula/flow-preferences/issues/15
        .collect {
          deferred.await()
          results.add(it)
        }
    }

    preference.set(20f)
    preference.set(30f)
    preference.set(50f)

    deferred.complete(Unit)
    job.cancel()

    assertThat(results).isEqualTo(listOf(0.5f, 50f))
  }

  @Test fun testCollector() = runTest {
    val preference = flowSharedPreferences.getInt("key", defaultValue = 1)

    preference.asCollector().emit(123)
    assertThat(preference.get()).isEqualTo(123)

    val flow = flow { emit(456) }
    preference.asCollector().emitAll(flow)
    assertThat(preference.get()).isEqualTo(456)
  }

  @Test fun testSyncCollector() = runTest {
    val preference = flowSharedPreferences.getString("key", defaultValue = "sync")

    preference.asSyncCollector().emit("xyz")
    assertThat(preference.get()).isEqualTo("xyz")

    val flow = flow { emit("abc") }
    preference.asSyncCollector().emitAll(flow)
    assertThat(preference.get()).isEqualTo("abc")
  }
}
