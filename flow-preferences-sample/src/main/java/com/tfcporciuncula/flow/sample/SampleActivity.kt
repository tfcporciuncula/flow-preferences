package com.tfcporciuncula.flow.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.tfcporciuncula.flow.FlowSharedPreferences
import kotlinx.android.synthetic.main.activity_sample.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class SampleActivity : AppCompatActivity(R.layout.activity_sample) {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    val flowSharedPreferences = FlowSharedPreferences(sharedPreferences)

    setupStringExample(flowSharedPreferences)
    setupIntCollectorExample(flowSharedPreferences)
  }

  private fun setupStringExample(flowSharedPreferences: FlowSharedPreferences) {
    val stringPreference = flowSharedPreferences.getString("stringPref", defaultValue = "[empty]")

    inputEditText.doOnTextChanged { text, _, _, _ ->
      text?.let { stringPreference.set(it.toString()) }
    }

    lifecycleScope.launch {
      stringPreference.asFlow().collect { outputTextView.text = it }
    }
  }

  private fun setupIntCollectorExample(flowSharedPreferences: FlowSharedPreferences) {
    val intPreference = flowSharedPreferences.getInt("intPref")

    lifecycleScope.launchWhenStarted {
      intPreference.asCollector().emitAll(getSecondsElapsedFlow())
    }
    lifecycleScope.launch {
      intPreference.asFlow().collect { secondsInForegroundTextView.text = it.toString() }
    }
  }

  private fun getSecondsElapsedFlow() =
    flow {
      var i = 0
      while (true) {
        emit(++i)
        delay(1000)
      }
    }
}
