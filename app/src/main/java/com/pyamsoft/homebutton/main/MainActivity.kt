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
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.pyamsoft.homebutton.BuildConfig
import com.pyamsoft.homebutton.HomeButtonComponent
import com.pyamsoft.homebutton.R
import com.pyamsoft.homebutton.R.mipmap
import com.pyamsoft.homebutton.R.style
import com.pyamsoft.homebutton.settings.SettingsFragment
import com.pyamsoft.pydroid.arch.UnitViewModel
import com.pyamsoft.pydroid.arch.createComponent
import com.pyamsoft.pydroid.ui.Injector
import com.pyamsoft.pydroid.ui.about.AboutFragment
import com.pyamsoft.pydroid.ui.rating.ChangeLogBuilder
import com.pyamsoft.pydroid.ui.rating.RatingActivity
import com.pyamsoft.pydroid.ui.rating.buildChangeLog
import com.pyamsoft.pydroid.ui.util.commit
import com.pyamsoft.pydroid.ui.util.layout
import com.pyamsoft.pydroid.ui.widget.shadow.DropshadowView
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

class MainActivity : RatingActivity() {

  @JvmField @Inject internal var mainFrameView: MainFrameView? = null
  @JvmField @Inject internal var toolbar: MainToolbarView? = null

  override val versionName: String = BuildConfig.VERSION_NAME

  override val applicationIcon: Int = mipmap.ic_launcher

  override val snackbarRoot: ViewGroup by lazy(NONE) {
    findViewById<CoordinatorLayout>(R.id.snackbar_root)
  }

  override val fragmentContainerId: Int
    get() = requireNotNull(mainFrameView).id()

  override val changeLogLines: ChangeLogBuilder = buildChangeLog {
    bugfix("Fixed notification not launching on device restart")
    change("Better open source license viewing experience")
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(style.Theme_HomeButton)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.snackbar_screen)

    val layoutRoot = findViewById<ConstraintLayout>(R.id.content_root)
    Injector.obtain<HomeButtonComponent>(applicationContext)
        .plusMain()
        .create(layoutRoot, this)
        .inject(this)

    val frameView = requireNotNull(mainFrameView)
    val toolbar = requireNotNull(toolbar)
    val dropshadow = DropshadowView.create(layoutRoot)

    createComponent(
        savedInstanceState, this,
        UnitViewModel.create(),
        frameView,
        toolbar,
        dropshadow
    ) {
    }

    layoutRoot.layout {
      toolbar.also {
        connect(it.id(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        connect(it.id(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        connect(it.id(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        constrainWidth(it.id(), ConstraintSet.MATCH_CONSTRAINT)
      }

      dropshadow.also {
        connect(it.id(), ConstraintSet.TOP, toolbar.id(), ConstraintSet.BOTTOM)
        connect(it.id(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        connect(it.id(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        constrainWidth(it.id(), ConstraintSet.MATCH_CONSTRAINT)
      }

      frameView.also {
        connect(it.id(), ConstraintSet.TOP, toolbar.id(), ConstraintSet.BOTTOM)
        connect(it.id(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        connect(it.id(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        connect(it.id(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        constrainWidth(it.id(), ConstraintSet.MATCH_CONSTRAINT)
        constrainHeight(it.id(), ConstraintSet.MATCH_CONSTRAINT)
      }
    }

    addPreferenceFragment()
  }

  override fun onDestroy() {
    super.onDestroy()
    mainFrameView = null
    toolbar = null
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    mainFrameView?.saveState(outState)
    toolbar?.saveState(outState)
  }

  private fun addPreferenceFragment() {
    val fm = supportFragmentManager
    if (fm.findFragmentByTag(SettingsFragment.TAG) == null && !AboutFragment.isPresent(this)) {
      fm.commit(this) {
        add(fragmentContainerId, SettingsFragment(), SettingsFragment.TAG)
      }
    }
  }
}
