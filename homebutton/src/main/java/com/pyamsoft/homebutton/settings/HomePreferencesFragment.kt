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
import android.view.View
import com.pyamsoft.homebutton.NotificationHandler
import com.pyamsoft.homebutton.R
import com.pyamsoft.pydroid.ui.app.requireToolbarActivity
import com.pyamsoft.pydroid.ui.settings.AppSettingsPreferenceFragment

class HomePreferencesFragment : AppSettingsPreferenceFragment(), SettingsView.Callback {

  private lateinit var settingsView: SettingsView
  private lateinit var toolbarView: ToolbarView

  override val hideClearAll: Boolean = true

  override val preferenceXmlResId: Int = R.xml.preferences

  private lateinit var notificationHandler: NotificationHandler

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    notificationHandler = NotificationHandler.create(requireActivity())

    settingsView = SettingsView(preferenceScreen, this)
    toolbarView = ToolbarView(requireToolbarActivity())
    settingsView.inflate(savedInstanceState)
  }

  override fun onShowNotificationChangeClicked(show: Boolean) {
    notificationHandler.start(show)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    settingsView.teardown()
    toolbarView.teardown()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    toolbarView.saveState(outState)
    settingsView.saveState(outState)
  }

  companion object {

    const val TAG = "HomePreferencesFragment"
  }
}
