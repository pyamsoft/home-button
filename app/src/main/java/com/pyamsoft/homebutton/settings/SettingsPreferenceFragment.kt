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

import android.os.Bundle
import android.view.View
import com.pyamsoft.homebutton.HomeButtonComponent
import com.pyamsoft.homebutton.R
import com.pyamsoft.homebutton.viewmodel.HomeButtonViewModelFactory
import com.pyamsoft.pydroid.arch.*
import com.pyamsoft.pydroid.ui.Injector
import com.pyamsoft.pydroid.ui.app.requireToolbarActivity
import com.pyamsoft.pydroid.ui.arch.fromViewModelFactory
import com.pyamsoft.pydroid.ui.settings.AppSettingsPreferenceFragment
import javax.inject.Inject

class SettingsPreferenceFragment :
    AppSettingsPreferenceFragment(), UiController<UnitControllerEvent> {

  override val preferenceXmlResId: Int = R.xml.preferences

  @JvmField @Inject internal var settingsView: SettingsView? = null

  @JvmField @Inject internal var toolbarView: SettingsToolbarView? = null

  @JvmField @Inject internal var spacer: SettingsSpacer? = null

  @JvmField @Inject internal var factory: HomeButtonViewModelFactory? = null
  private val viewModel by fromViewModelFactory<SettingsViewModel> { factory?.create(this) }

  private var stateSaver: StateSaver? = null

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    Injector.obtainFromApplication<HomeButtonComponent>(view.context)
        .plusSettings()
        .create(requireActivity(), requireToolbarActivity(), preferenceScreen)
        .inject(this)

    stateSaver =
        createComponent(
            savedInstanceState,
            viewLifecycleOwner,
            viewModel,
            this,
            requireNotNull(settingsView),
            requireNotNull(toolbarView),
            requireNotNull(spacer)) {
          return@createComponent when (it) {
            is SettingsViewEvent.NotificationVisibility ->
                viewModel.handleVisibilityEvent(it.isVisible)
            is SettingsViewEvent.OpenNotificationSettings ->
                viewModel.handleOpenSettings(this, requireActivity())
          }
        }
  }

  override fun onControllerEvent(event: UnitControllerEvent) {}

  override fun onDestroyView() {
    super.onDestroyView()

    toolbarView = null
    settingsView = null
    factory = null
    stateSaver = null
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    stateSaver?.saveState(outState)
  }

  companion object {

    const val TAG = "SettingsPreferenceFragment"
  }
}
