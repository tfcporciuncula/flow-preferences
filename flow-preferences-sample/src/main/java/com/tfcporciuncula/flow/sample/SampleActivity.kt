package com.tfcporciuncula.flow.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.tfcporciuncula.flow.FlowSharedPreferences
import com.tfcporciuncula.flow.sample.databinding.SampleActivityBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class SampleActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding = SampleActivityBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    val flowSharedPreferences = FlowSharedPreferences(sharedPreferences)

    setupStringExample(binding, flowSharedPreferences)
    setupIntCollectorExample(binding, flowSharedPreferences)
  }

  private fun setupStringExample(binding: SampleActivityBinding, flowSharedPreferences: FlowSharedPreferences) {
    val stringPreference = flowSharedPreferences.getString("stringPref", defaultValue = "[empty]")

    binding.inputEditText.doOnTextChanged { text, _, _, _ ->
      text?.let { stringPreference.set(it.toString()) }
    }

    lifecycleScope.launch {
      stringPreference.asFlow().collect { binding.outputTextView.text = it }
    }
  }

  private fun setupIntCollectorExample(binding: SampleActivityBinding, flowSharedPreferences: FlowSharedPreferences) {
    val intPreference = flowSharedPreferences.getInt("intPref")

    lifecycleScope.launchWhenStarted {
      intPreference.asCollector().emitAll(getSecondsElapsedFlow())
    }
    lifecycleScope.launch {
      intPreference.asFlow().collect { binding.secondsInForegroundTextView.text = it.toString() }
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
