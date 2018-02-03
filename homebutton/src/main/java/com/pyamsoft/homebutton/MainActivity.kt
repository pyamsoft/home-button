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
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import com.pyamsoft.homebutton.databinding.ActivityMainBinding
import com.pyamsoft.pydroid.ui.about.AboutLibrariesFragment
import com.pyamsoft.pydroid.ui.helper.DebouncedOnClickListener
import com.pyamsoft.pydroid.ui.sec.TamperActivity
import com.pyamsoft.pydroid.util.AppUtil
import com.pyamsoft.homebutton.R
import com.pyamsoft.homebutton.BuildConfig

class MainActivity : TamperActivity() {

  private lateinit var binding: ActivityMainBinding

  override val safePackageName: String = "com.pyamsoft.homebutton"

  override val changeLogLines: Array<String> = arrayOf(
      "BUGFIX: Bugfixes and optimizations"
  )

  override val versionName: String = BuildConfig.VERSION_NAME

  override val applicationIcon: Int = R.mipmap.ic_launcher

  override val currentApplicationVersion: Int = BuildConfig.VERSION_CODE

  override val applicationName: String
    get() = getString(R.string.app_name)

  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(R.style.Theme_HomeButton_Light)
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

    setupToolbar()
    addPreferenceFragment()
  }

  private fun addPreferenceFragment() {
    val fm = supportFragmentManager
    val aboutLibrariesFragment: Fragment? = fm.findFragmentByTag(AboutLibrariesFragment.TAG)
    if (fm.findFragmentByTag(HomeFragment.TAG) == null && aboutLibrariesFragment == null) {
      fm.beginTransaction()
          .add(R.id.main_view_container, HomeFragment(), HomeFragment.TAG)
          .commit()
    }
  }

  private fun setupToolbar() {
    binding.toolbar.apply {
      setToolbar(this)
      setTitle(R.string.app_name)
      ViewCompat.setElevation(this, AppUtil.convertToDP(context, 4f))

      setNavigationOnClickListener(DebouncedOnClickListener.create {
        onBackPressed()
      })
    }
  }
}
