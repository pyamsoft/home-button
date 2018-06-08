/*
 * Copyright (C) 2018 Peter Kenji Yamanaka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pyamsoft.homebutton

import android.os.Bundle
import android.view.View
import com.pyamsoft.pydroid.ui.app.fragment.SettingsPreferenceFragment
import com.pyamsoft.pydroid.ui.util.setUpEnabled

class HomePreferencesFragment : SettingsPreferenceFragment() {

  override val applicationName: String
    get() = getString(R.string.app_name)

  override val rootViewContainer: Int = R.id.main_view_container

  override val hideClearAll: Boolean = true

  override val preferenceXmlResId: Int = R.xml.preferences

  override fun onDestroy() {
    super.onDestroy()
    HomeButton.getRefWatcher(this)
        .watch(this)
  }

  override fun onResume() {
    super.onResume()
    toolbarActivity.withToolbar {
      it.setTitle(R.string.app_name)
      it.setUpEnabled(false)
    }
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    findPreference(getString(R.string.priority_key)).setOnPreferenceChangeListener { _, newValue ->
      if (newValue is Boolean) {
        HomeButton.notificationHandler(view.context)
            .start(newValue)
        return@setOnPreferenceChangeListener true
      } else {
        return@setOnPreferenceChangeListener false
      }
    }
  }

  companion object {

    const val TAG = "HomePreferencesFragment"
  }
}
