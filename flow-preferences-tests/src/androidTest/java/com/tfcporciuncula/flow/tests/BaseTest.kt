package com.tfcporciuncula.flow.tests

import androidx.preference.PreferenceManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tfcporciuncula.flow.FlowSharedPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
abstract class BaseTest {

  private val context = InstrumentationRegistry.getInstrumentation().targetContext
  private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
  protected val flowSharedPreferences = FlowSharedPreferences(sharedPreferences)

  @Before fun setup() = flowSharedPreferences.clear()
}
