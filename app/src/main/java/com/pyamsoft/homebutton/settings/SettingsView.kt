/*
 * Copyright 2020 Peter Kenji Yamanaka
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
 */

package com.pyamsoft.homebutton.settings

import androidx.preference.Preference
import androidx.preference.PreferenceScreen
import com.pyamsoft.homebutton.R
import com.pyamsoft.homebutton.settings.SettingsViewEvent.NotificationVisibility
import com.pyamsoft.pydroid.arch.UnitViewState
import com.pyamsoft.pydroid.ui.arch.PrefUiView
import javax.inject.Inject

internal class SettingsView @Inject internal constructor(
    preferenceScreen: PreferenceScreen
) : PrefUiView<UnitViewState, SettingsViewEvent>(preferenceScreen) {

    private val homePref by boundPref<Preference>(R.string.priority_key)

    init {
        doOnInflate {
            setupShowNotification()
        }

        doOnTeardown {
            homePref.onPreferenceChangeListener = null
        }
    }

    override fun onRender(state: UnitViewState) {
    }

    private fun setupShowNotification() {
        homePref.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
            if (newValue is Boolean) {
                publish(NotificationVisibility(newValue))
                return@OnPreferenceChangeListener true
            } else {
                return@OnPreferenceChangeListener false
            }
        }
    }
}
