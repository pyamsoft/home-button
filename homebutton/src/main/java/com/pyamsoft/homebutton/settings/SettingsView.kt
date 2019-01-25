/*
 * Copyright 2019 Peter Kenji Yamanaka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.pyamsoft.homebutton.settings

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceScreen
import com.pyamsoft.homebutton.R
import com.pyamsoft.homebutton.settings.SettingsViewEvent.ShowNotification
import com.pyamsoft.pydroid.core.bus.Publisher
import com.pyamsoft.pydroid.ui.arch.InvalidUiComponentIdException
import com.pyamsoft.pydroid.ui.arch.PrefUiView

internal class SettingsView internal constructor(
  preferenceScreen: PreferenceScreen,
  uiBus: Publisher<SettingsViewEvent>
) : PrefUiView<SettingsViewEvent>(preferenceScreen, uiBus) {

  private val homePref by lazyPref<Preference>(R.string.priority_key)

  override fun inflate(savedInstanceState: Bundle?) {
    setupShowNotification()
  }

  override fun teardown() {
    homePref.onPreferenceChangeListener = null
  }

  private fun setupShowNotification() {
    homePref.setOnPreferenceChangeListener { _, newValue ->
      if (newValue is Boolean) {
        publish(ShowNotification(newValue))
        return@setOnPreferenceChangeListener true
      } else {
        return@setOnPreferenceChangeListener false
      }
    }
  }

}
