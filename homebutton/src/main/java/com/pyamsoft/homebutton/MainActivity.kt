/*
 * Copyright (C) 2018 Peter Kenji Yamanaka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
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
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import com.pyamsoft.homebutton.databinding.ActivityMainBinding
import com.pyamsoft.pydroid.ui.about.AboutLibrariesFragment
import com.pyamsoft.pydroid.ui.helper.DebouncedOnClickListener
import com.pyamsoft.pydroid.ui.sec.TamperActivity
import com.pyamsoft.pydroid.util.AppUtil

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
