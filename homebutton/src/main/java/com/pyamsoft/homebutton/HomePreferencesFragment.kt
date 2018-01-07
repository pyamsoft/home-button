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

import android.support.v4.app.Fragment
import com.pyamsoft.pydroid.ui.app.fragment.SettingsPreferenceFragment
import com.pyamsoft.pydroid.ui.util.setUpEnabled

class HomePreferencesFragment : SettingsPreferenceFragment() {

    override val applicationName: String
        get() = getString(R.string.app_name)

    override val rootViewContainer: Int = R.id.main_view_container

    override val hideClearAll: Boolean = true

    override val aboutReplaceFragment: Fragment?
        // Must be here so its pulled on demand instead of assigned
        get() = parentFragment

    override fun onDestroy() {
        super.onDestroy()
        HomeButton.getRefWatcher(this).watch(this)
    }

    override fun onResume() {
        super.onResume()
        toolbarActivity.withToolbar {
            it.setTitle(R.string.app_name)
            it.setUpEnabled(false)
        }
    }

    companion object {

        const val TAG = "HomePreferencesFragment"
    }
}
