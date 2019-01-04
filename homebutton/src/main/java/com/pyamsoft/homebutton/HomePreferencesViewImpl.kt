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

package com.pyamsoft.homebutton

import androidx.preference.PreferenceScreen

internal class HomePreferencesViewImpl internal constructor(
  private val preferenceScreen: PreferenceScreen
) : HomePreferencesView {

  private val context = preferenceScreen.context

  override fun create() {
  }

  override fun onShowNotificationClicked(onClick: (show: Boolean) -> Unit) {
    val homePref = preferenceScreen.findPreference(context.getString(R.string.priority_key))

    homePref.setOnPreferenceChangeListener { _, newValue ->
      if (newValue is Boolean) {
        onClick(newValue)
        return@setOnPreferenceChangeListener true
      } else {
        return@setOnPreferenceChangeListener false
      }
    }
  }

}