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
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private const val ONE_SECOND_DELAY = 1000L

@ExperimentalCoroutinesApi
class SampleActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding = SampleActivityBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    val flowSharedPreferences = FlowSharedPreferences(sharedPreferences)

    binding.clearButton.setOnClickListener { flowSharedPreferences.clear() }

    setupStringExample(binding, flowSharedPreferences)
    setupIntCollectorExample(binding, flowSharedPreferences)
  }

  private fun setupStringExample(binding: SampleActivityBinding, flowSharedPreferences: FlowSharedPreferences) {
    val stringPreference = flowSharedPreferences.getString("stringPref", defaultValue = "[empty]")

    binding.inputEditText.doOnTextChanged { text, _, _, _ ->
      text?.let { stringPreference.set(it.toString()) }
    }

    stringPreference.asFlow().onEach { binding.outputTextView.text = it }.launchIn(lifecycleScope)
  }

  private fun setupIntCollectorExample(binding: SampleActivityBinding, flowSharedPreferences: FlowSharedPreferences) {
    val intPreference = flowSharedPreferences.getInt("intPref")

    lifecycleScope.launchWhenStarted {
      intPreference.asCollector().emitAll(getSecondsElapsedFlow())
    }
    intPreference.asFlow().onEach { binding.secondsInForegroundTextView.text = it.toString() }.launchIn(lifecycleScope)
  }

  private fun getSecondsElapsedFlow() =
    flow {
      var i = 0
      while (true) {
        emit(++i)
        delay(ONE_SECOND_DELAY)
      }
    }
}
