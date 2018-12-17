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
import com.pyamsoft.pydroid.ui.widget.resize.FluidContentResizer
import com.pyamsoft.pydroid.ui.about.AboutFragment
import com.pyamsoft.pydroid.ui.rating.ChangeLogBuilder
import com.pyamsoft.pydroid.ui.rating.RatingActivity
import com.pyamsoft.pydroid.ui.rating.buildChangeLog
import com.pyamsoft.pydroid.ui.util.commit

class MainActivity : RatingActivity() {

  private lateinit var homeView: HomeView

  override val versionName: String = BuildConfig.VERSION_NAME

  override val applicationIcon: Int = R.mipmap.ic_launcher

  override val rootView: View
    get() = homeView.root()

  override val changeLogLines: ChangeLogBuilder = buildChangeLog {
    change("New icon style")
    change("Better open source license viewing experience")
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    if (HomeButton.theming(this).isDarkTheme()) {
      setTheme(R.style.Theme_HomeButton_Dark)
    } else {
      setTheme(R.style.Theme_HomeButton_Light)
    }
    super.onCreate(savedInstanceState)

    homeView = HomeViewImpl(this)
    homeView.create()

    FluidContentResizer.listen(this)

    addPreferenceFragment()
    setupToolbar()
  }

  private fun addPreferenceFragment() {
    val fm = supportFragmentManager
    if (fm.findFragmentByTag(HomeFragment.TAG) == null && !AboutFragment.isPresent(this)) {
      fm.beginTransaction()
          .add(R.id.main_view_container, HomeFragment(), HomeFragment.TAG)
          .commit(this)
    }
  }

  private fun setupToolbar() {
    homeView.onToolbarNavClicked { onBackPressed() }
  }
}
