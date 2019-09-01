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
import androidx.lifecycle.ViewModelProvider
import com.pyamsoft.homebutton.HomeButtonComponent
import com.pyamsoft.homebutton.NotificationHandler
import com.pyamsoft.homebutton.R
import com.pyamsoft.homebutton.settings.SettingsControllerEvent.NotificationChanged
import com.pyamsoft.pydroid.arch.createComponent
import com.pyamsoft.pydroid.ui.Injector
import com.pyamsoft.pydroid.ui.app.requireToolbarActivity
import com.pyamsoft.pydroid.ui.arch.factory
import com.pyamsoft.pydroid.ui.settings.AppSettingsPreferenceFragment
import javax.inject.Inject

class SettingsPreferenceFragment : AppSettingsPreferenceFragment() {

    override val hideClearAll: Boolean = true

    override val preferenceXmlResId: Int = R.xml.preferences

    @JvmField
    @Inject
    internal var notificationHandler: NotificationHandler? = null

    @JvmField
    @Inject
    internal var factory: ViewModelProvider.Factory? = null
    @JvmField
    @Inject
    internal var settingsView: SettingsView? = null
    @JvmField
    @Inject
    internal var toolbarView: SettingsToolbarView? = null
    private val viewModel by factory<SettingsViewModel> { factory }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        Injector.obtain<HomeButtonComponent>(view.context.applicationContext)
            .plusSettings()
            .create(requireToolbarActivity(), preferenceScreen)
            .inject(this)

        createComponent(
            savedInstanceState, viewLifecycleOwner,
            viewModel,
            requireNotNull(settingsView),
            requireNotNull(toolbarView)
        ) {
            return@createComponent when (it) {
                is NotificationChanged -> handleNotificationVisibilityChange(it.isVisible)
            }
        }

        requireNotNull(notificationHandler).start()
    }

    private fun handleNotificationVisibilityChange(visible: Boolean) {
        requireNotNull(notificationHandler).start(visible)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        toolbarView = null
        settingsView = null
        notificationHandler = null
        factory = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        settingsView?.saveState(outState)
        toolbarView?.saveState(outState)
    }

    companion object {

        const val TAG = "SettingsPreferenceFragment"
    }
}
