/*
 * Copyright 2017 Peter Kenji Yamanaka
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

package com.pyamsoft.homebutton

import com.pyamsoft.pydroid.ui.about.AboutLibrariesFragment
import com.pyamsoft.pydroid.ui.app.fragment.ActionBarSettingsPreferenceFragment
import com.pyamsoft.pydroid.ui.util.ActionBarUtil

class HomePreferencesFragment : ActionBarSettingsPreferenceFragment() {

  override val isLastOnBackStack: AboutLibrariesFragment.BackStackState
    get() = AboutLibrariesFragment.BackStackState.LAST

  override val rootViewContainer: Int
    get() = R.id.main_view_container

  override val applicationName: String
    get() = getString(R.string.app_name)

  override val hideClearAll: Boolean
    get() = true

  override fun onDestroy() {
    super.onDestroy()
    HomeButton.getRefWatcher(this).watch(this)
  }

  override fun onResume() {
    super.onResume()
    ActionBarUtil.setActionBarTitle(activity, getString(R.string.app_name))
  }

  companion object {

    const val TAG = "HomePreferencesFragment"
  }
}
