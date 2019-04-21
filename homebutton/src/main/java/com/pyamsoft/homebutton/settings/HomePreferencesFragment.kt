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
import com.pyamsoft.homebutton.HomeButtonComponent
import com.pyamsoft.homebutton.NotificationHandler
import com.pyamsoft.homebutton.R
import com.pyamsoft.pydroid.ui.Injector
import com.pyamsoft.pydroid.ui.app.requireToolbarActivity
import com.pyamsoft.pydroid.ui.settings.AppSettingsPreferenceFragment
import javax.inject.Inject

class HomePreferencesFragment : AppSettingsPreferenceFragment(), SettingsUiComponent.Callback {

  override val hideClearAll: Boolean = true

  override val preferenceXmlResId: Int = R.xml.preferences

  @JvmField @Inject internal var component: SettingsUiComponent? = null
  @JvmField @Inject internal var notificationHandler: NotificationHandler? = null

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    Injector.obtain<HomeButtonComponent>(view.context.applicationContext)
        .plusSettings()
        .create(requireToolbarActivity(), preferenceScreen)
        .inject(this)

    requireNotNull(component).bind(viewLifecycleOwner, savedInstanceState, this)
    requireNotNull(notificationHandler).start()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    component = null
    notificationHandler = null
  }

  override fun onShowNotificationChanged(show: Boolean) {
    requireNotNull(notificationHandler).start(show)
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    component?.saveState(outState)
  }

  companion object {

    const val TAG = "HomePreferencesFragment"
  }
}
