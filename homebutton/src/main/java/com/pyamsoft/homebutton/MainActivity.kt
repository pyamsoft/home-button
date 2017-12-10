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

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.view.MenuItem
import com.pyamsoft.backstack.BackStack
import com.pyamsoft.homebutton.databinding.ActivityMainBinding
import com.pyamsoft.pydroid.ui.about.AboutLibrariesFragment
import com.pyamsoft.pydroid.ui.sec.TamperActivity
import com.pyamsoft.pydroid.util.AppUtil

class MainActivity : TamperActivity() {

    private lateinit var binding: ActivityMainBinding

    override val safePackageName: String = "com.pyamsoft.homebutton"

    override val changeLogLines: Array<String> = arrayOf(
            "BUGFIX: Better support for small screen devices"
    )

    override val versionName: String = BuildConfig.VERSION_NAME

    override val applicationIcon: Int = R.mipmap.ic_launcher

    override val currentApplicationVersion: Int = BuildConfig.VERSION_CODE

    override val applicationName: String
        get() = getString(R.string.app_name)

    private lateinit var backstack: BackStack

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_HomeButton_Light)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        backstack = BackStack.create(this, R.id.main_view_container)

        setupToolbar()
        addPreferenceFragment()
    }

    private fun addPreferenceFragment() {
        val fragmentManager = supportFragmentManager
        if (fragmentManager.findFragmentByTag(HomeFragment.TAG) == null
                && fragmentManager.findFragmentByTag(AboutLibrariesFragment.TAG) == null) {
            backstack.set(HomeFragment.TAG) { HomeFragment() }
        }
    }

    override fun onBackPressed() {
        if (!backstack.back()) {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        val handled: Boolean = when (itemId) {
            android.R.id.home -> {
                onBackPressed()

                // Assign
                true
            }
            else -> false
        }
        return handled || super.onOptionsItemSelected(item)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = getString(R.string.app_name)
        ViewCompat.setElevation(binding.toolbar, AppUtil.convertToDP(this, 4f))
    }
}
