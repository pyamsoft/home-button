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
import androidx.lifecycle.LifecycleOwner
import com.pyamsoft.homebutton.settings.SettingsUiComponent.Callback
import com.pyamsoft.homebutton.settings.SettingsViewModel.SettingsState
import com.pyamsoft.pydroid.arch.BaseUiComponent
import com.pyamsoft.pydroid.arch.doOnDestroy
import javax.inject.Inject

internal class SettingsUiComponentImpl @Inject internal constructor(
  private val settingsView: SettingsView,
  private val toolbarView: ToolbarView,
  private val viewModel: SettingsViewModel
) : BaseUiComponent<Callback>(),
    SettingsUiComponent {

  override fun id(): Int {
    return settingsView.id()
  }

  override fun onBind(
    owner: LifecycleOwner,
    savedInstanceState: Bundle?,
    callback: Callback
  ) {
    owner.doOnDestroy {
      settingsView.teardown()
      toolbarView.teardown()
      viewModel.unbind()
    }

    settingsView.inflate(savedInstanceState)
    toolbarView.inflate(savedInstanceState)
    viewModel.bind { state, oldState ->
      renderShowing(state, oldState)
    }
  }

  override fun onSaveState(outState: Bundle) {
    settingsView.saveState(outState)
    toolbarView.saveState(outState)
  }

  private fun renderShowing(
    state: SettingsState,
    oldState: SettingsState?
  ) {
    state.renderOnChange(oldState, value = { it.isShowing }) { isShowing ->
      if (isShowing != null) {
        callback.onShowNotificationChanged(isShowing.isShowing)
      }
    }
  }
}

