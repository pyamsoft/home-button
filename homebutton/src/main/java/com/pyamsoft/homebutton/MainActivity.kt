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

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import com.pyamsoft.homebutton.databinding.ActivityMainBinding
import com.pyamsoft.pydroid.ui.about.AboutLibrariesFragment
import com.pyamsoft.pydroid.ui.bugreport.BugreportDialog
import com.pyamsoft.pydroid.ui.rating.ChangeLogBuilder
import com.pyamsoft.pydroid.ui.rating.RatingActivity
import com.pyamsoft.pydroid.ui.rating.buildChangeLog
import com.pyamsoft.pydroid.ui.util.DebouncedOnClickListener
import com.pyamsoft.pydroid.util.toDp

class MainActivity : RatingActivity() {

  private lateinit var binding: ActivityMainBinding

  override val versionName: String = BuildConfig.VERSION_NAME

  override val applicationIcon: Int = R.mipmap.ic_launcher

  override val currentApplicationVersion: Int = BuildConfig.VERSION_CODE

  override val applicationName: String
    get() = getString(R.string.app_name)

  override val rootView: View
    get() = binding.root

  override val changeLogLines: ChangeLogBuilder = buildChangeLog {
    bugfix("Stability updates and bugfixes")
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(R.style.Theme_HomeButton_Light)
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

    setupToolbar()
    addPreferenceFragment()
    BugreportDialog.attachToToolbar(this, applicationName, currentApplicationVersion)
  }

  private fun addPreferenceFragment() {
    val fm = supportFragmentManager
    if (fm.findFragmentByTag(HomeFragment.TAG) == null && !AboutLibrariesFragment.isPresent(this)) {
      fm.beginTransaction()
          .add(R.id.main_view_container, HomeFragment(), HomeFragment.TAG)
          .commit()
    }
  }

  private fun setupToolbar() {
    binding.toolbar.apply {
      setToolbar(this)
      setTitle(R.string.app_name)
      ViewCompat.setElevation(this, 4f.toDp(context).toFloat())

      setNavigationOnClickListener(DebouncedOnClickListener.create {
        onBackPressed()
      })
    }
  }
}
