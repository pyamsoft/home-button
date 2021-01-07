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

import android.app.Activity
import androidx.lifecycle.viewModelScope
import com.pyamsoft.homebutton.notification.NotificationHandler
import com.pyamsoft.homebutton.settings.SettingsViewEvent.NotificationVisibility
import com.pyamsoft.pydroid.arch.UiViewModel
import com.pyamsoft.pydroid.arch.UnitControllerEvent
import com.pyamsoft.pydroid.arch.UnitViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class SettingsViewModel @Inject internal constructor(
    private val notificationHandler: NotificationHandler
) : UiViewModel<UnitViewState, SettingsViewEvent, UnitControllerEvent>(UnitViewState) {

    init {
        viewModelScope.launch(context = Dispatchers.Default) {
            notificationHandler.start()
        }
    }

    override fun handleViewEvent(event: SettingsViewEvent) {
        return when (event) {
            is NotificationVisibility -> onVisibilityEvent(event.isVisible)
            is SettingsViewEvent.OpenNotificationSettings -> openSettings(event.activity)
        }
    }

    private fun openSettings(activity: Activity) {
        viewModelScope.launch(context = Dispatchers.Default) {
            notificationHandler.openSettings(activity)
        }
    }

    private fun onVisibilityEvent(visible: Boolean) {
        viewModelScope.launch(context = Dispatchers.Default) {
            notificationHandler.start(show = visible)
        }
    }
}
