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

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.view.MenuItem
import com.pyamsoft.homebutton.databinding.ActivityMainBinding
import com.pyamsoft.pydroid.ui.about.AboutLibrariesFragment
import com.pyamsoft.pydroid.ui.sec.TamperActivity
import com.pyamsoft.pydroid.util.AppUtil

class MainActivity : TamperActivity() {

  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(R.style.Theme_HomeButton_Light)
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

    setupToolbar()
    addPreferenceFragment()
  }

  private fun addPreferenceFragment() {
    val fragmentManager = supportFragmentManager
    if (fragmentManager.findFragmentByTag(
        HomePreferencesFragment.TAG) == null && fragmentManager.findFragmentByTag(
        AboutLibrariesFragment.TAG) == null) {
      fragmentManager.beginTransaction().add(R.id.main_view_container, HomePreferencesFragment(),
          HomePreferencesFragment.TAG).commit()
    }
  }

  override fun onBackPressed() {
    val fragmentManager = supportFragmentManager
    val backStackCount = fragmentManager.backStackEntryCount
    if (backStackCount > 0) {
      fragmentManager.popBackStack()
    } else {
      super.onBackPressed()
    }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    val itemId = item.itemId
    val handled: Boolean
    when (itemId) {
      android.R.id.home -> {
        onBackPressed()
        handled = true
      }
      else -> handled = false
    }
    return handled || super.onOptionsItemSelected(item)
  }

  private fun setupToolbar() {
    setSupportActionBar(binding.toolbar)
    binding.toolbar.title = getString(R.string.app_name)
    ViewCompat.setElevation(binding.toolbar, AppUtil.convertToDP(this, 4f))
  }

  override val safePackageName: String
    get() = "com.pyamsoft.homebutton"

  override val changeLogLines: Array<String>
    get() {
      val line1 = "BUGFIX: Bugfixes and improvements"
      val line2 = "BUGFIX: Removed all Advertisements"
      val line3 = "BUGFIX: Faster loading of Open Source Licenses page"
      return arrayOf(line1, line2, line3)
    }

  override val versionName: String
    get() = BuildConfig.VERSION_NAME

  override val applicationIcon: Int
    get() = R.mipmap.ic_launcher

  override val applicationName: String
    get() = getString(R.string.app_name)

  override val currentApplicationVersion: Int
    get() = BuildConfig.VERSION_CODE
}
