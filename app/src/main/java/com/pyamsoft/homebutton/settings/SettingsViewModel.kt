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

import androidx.lifecycle.viewModelScope
import com.pyamsoft.homebutton.NotificationHandler
import com.pyamsoft.homebutton.settings.SettingsViewEvent.NotificationVisibility
import com.pyamsoft.pydroid.arch.UiViewModel
import com.pyamsoft.pydroid.arch.UnitControllerEvent
import com.pyamsoft.pydroid.arch.UnitViewState
import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class SettingsViewModel @Inject internal constructor(
    @Named("debug") debug: Boolean,
    private val notificationHandler: NotificationHandler
) : UiViewModel<UnitViewState, SettingsViewEvent, UnitControllerEvent>(
    initialState = UnitViewState, debug = debug
) {

    init {
        doOnInit {
            viewModelScope.launch(context = Dispatchers.Default) {
                notificationHandler.start()
            }
        }
    }

    override fun handleViewEvent(event: SettingsViewEvent) {
        return when (event) {
            is NotificationVisibility -> onVisibilityEvent(event.isVisible)
        }
    }

    private fun onVisibilityEvent(visible: Boolean) {
        viewModelScope.launch(context = Dispatchers.Default) {
            notificationHandler.start(showNotification = visible)
        }
    }
}
