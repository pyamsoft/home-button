/*
 *     Copyright (C) 2017 Peter Kenji Yamanaka
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
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
        ActionBarUtil.setActionBarTitle(activity!!, getString(R.string.app_name))
    }

    companion object {

        const val TAG = "HomePreferencesFragment"
    }
}
