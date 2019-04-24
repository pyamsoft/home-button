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

import com.pyamsoft.homebutton.settings.SettingsHandler.SettingsEvent
import com.pyamsoft.homebutton.settings.SettingsViewModel.SettingsState
import com.pyamsoft.homebutton.settings.SettingsViewModel.SettingsState.Showing
import com.pyamsoft.pydroid.arch.UiEventHandler
import com.pyamsoft.pydroid.arch.UiState
import com.pyamsoft.pydroid.arch.UiViewModel
import javax.inject.Inject

internal class SettingsViewModel @Inject internal constructor(
  private val handler: UiEventHandler<SettingsEvent, SettingsView.Callback>
) : UiViewModel<SettingsState>(initialState = SettingsState(isShowing = null)),
    SettingsView.Callback {

  override fun onShowNotificationChangeClicked(show: Boolean) {
    setState {
      copy(isShowing = Showing(show))
    }
  }

  override fun onBind() {
    handler.handle(this)
        .disposeOnDestroy()
  }

  override fun onUnbind() {
  }

  data class SettingsState(val isShowing: Showing?) : UiState {
    data class Showing(val isShowing: Boolean)
  }
}
