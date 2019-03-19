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

package com.pyamsoft.homebutton.main

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.pyamsoft.homebutton.BuildConfig
import com.pyamsoft.homebutton.R
import com.pyamsoft.homebutton.R.mipmap
import com.pyamsoft.homebutton.R.style
import com.pyamsoft.homebutton.settings.HomeFragment
import com.pyamsoft.pydroid.arch.layout
import com.pyamsoft.pydroid.ui.about.AboutFragment
import com.pyamsoft.pydroid.ui.rating.ChangeLogBuilder
import com.pyamsoft.pydroid.ui.rating.RatingActivity
import com.pyamsoft.pydroid.ui.rating.buildChangeLog
import com.pyamsoft.pydroid.ui.theme.ThemeInjector
import com.pyamsoft.pydroid.ui.util.commit
import com.pyamsoft.pydroid.ui.widget.shadow.DropshadowView
import kotlin.LazyThreadSafetyMode.NONE

class MainActivity : RatingActivity() {

  private lateinit var component: MainUiComponent
  private lateinit var toolbarComponent: MainToolbarUiComponent

  override val versionName: String = BuildConfig.VERSION_NAME

  override val applicationIcon: Int = mipmap.ic_launcher

  override val snackbarRoot: View by lazy(NONE) {
    findViewById<CoordinatorLayout>(R.id.snackbar_root)
  }

  override val fragmentContainerId: Int
    get() = component.id()

  override val changeLogLines: ChangeLogBuilder = buildChangeLog {
    change("New icon style")
    change("Better open source license viewing experience")
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    if (ThemeInjector.obtain(applicationContext).isDarkTheme()) {
      setTheme(style.Theme_HomeButton_Dark)
    } else {
      setTheme(style.Theme_HomeButton_Light)
    }
    super.onCreate(savedInstanceState)
    setContentView(R.layout.snackbar_screen)

    val layoutRoot = findViewById<ConstraintLayout>(R.id.content_root)

    val dropshadow = DropshadowView(layoutRoot)
    val toolbar = MainToolbarView(layoutRoot, this)
    val frameView = MainFrameView(layoutRoot)
    toolbarComponent = MainToolbarUiComponentImpl(dropshadow, toolbar)
    component = MainUiComponentImpl(frameView)

    component.bind(layoutRoot, this, savedInstanceState, Unit)
    toolbarComponent.bind(layoutRoot, this, savedInstanceState, Unit)

    layoutRoot.layout {
      toolbarComponent.also {
        connect(it.id(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        connect(it.id(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        connect(it.id(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        constrainWidth(it.id(), ConstraintSet.MATCH_CONSTRAINT)
      }

      component.also {
        connect(it.id(), ConstraintSet.TOP, toolbarComponent.id(), ConstraintSet.BOTTOM)
        connect(it.id(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        connect(it.id(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        connect(it.id(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        constrainWidth(it.id(), ConstraintSet.MATCH_CONSTRAINT)
        constrainHeight(it.id(), ConstraintSet.MATCH_CONSTRAINT)
      }
    }

    addPreferenceFragment()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    toolbarComponent.saveState(outState)
    component.saveState(outState)
  }

  private fun addPreferenceFragment() {
    val fm = supportFragmentManager
    if (fm.findFragmentByTag(HomeFragment.TAG) == null && !AboutFragment.isPresent(this)) {
      fm.beginTransaction()
          .add(fragmentContainerId, HomeFragment(), HomeFragment.TAG)
          .commit(this)
    }
  }
}
