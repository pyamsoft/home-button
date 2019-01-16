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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pyamsoft.homebutton.HomeButton
import com.pyamsoft.homebutton.R
import com.pyamsoft.homebutton.main.MainActivity
import com.pyamsoft.homebutton.settings.SettingsViewEvent.ShowNotification
import com.pyamsoft.pydroid.core.bus.RxBus
import com.pyamsoft.pydroid.ui.app.fragment.requireToolbarActivity
import com.pyamsoft.pydroid.ui.arch.destroy
import com.pyamsoft.pydroid.ui.settings.AppSettingsPreferenceFragment
import com.pyamsoft.pydroid.ui.util.setUpEnabled

class HomePreferencesFragment : AppSettingsPreferenceFragment() {

  private val bus = RxBus.create<SettingsViewEvent>()
  private lateinit var settingsComponent: SettingsUiComponent

  override val fragmentContainerId: Int
    get() {
      val id = (requireActivity() as? MainActivity)?.fragmentContainerId ?: 0
      require(id > 0) { "Could not find fragmentContainerId" }
      return id
    }

  override val hideClearAll: Boolean = true

  override val preferenceXmlResId: Int = R.xml.preferences

  override fun onResume() {
    super.onResume()
    requireToolbarActivity().withToolbar {
      it.setTitle(R.string.app_name)
      it.setUpEnabled(false)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val settingsView = SettingsView(preferenceScreen, bus)
    settingsComponent = SettingsUiComponent(settingsView, bus, viewLifecycleOwner)
    return super.onCreateView(inflater, container, savedInstanceState)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    settingsComponent.onUiEvent()
        .subscribe {
          return@subscribe when (it) {
            is ShowNotification -> showNotification(it.visible)
          }
        }
        .destroy(viewLifecycleOwner)

    settingsComponent.create(savedInstanceState)
  }

  private fun showNotification(visible: Boolean) {
    HomeButton.notificationHandler(
        requireContext().applicationContext
    )
        .start(visible)
  }

  companion object {

    const val TAG = "HomePreferencesFragment"
  }
}
