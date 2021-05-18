/*
 * Copyright 2020 Peter Kenji Yamanaka
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

package com.pyamsoft.homebutton.main

import android.os.Bundle
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.pyamsoft.homebutton.BuildConfig
import com.pyamsoft.homebutton.HomeButtonComponent
import com.pyamsoft.homebutton.R
import com.pyamsoft.homebutton.settings.SettingsFragment
import com.pyamsoft.homebutton.viewmodel.HomeButtonViewModelFactory
import com.pyamsoft.pydroid.arch.StateSaver
import com.pyamsoft.pydroid.arch.UiController
import com.pyamsoft.pydroid.arch.UnitControllerEvent
import com.pyamsoft.pydroid.arch.createComponent
import com.pyamsoft.pydroid.ui.Injector
import com.pyamsoft.pydroid.ui.arch.fromViewModelFactory
import com.pyamsoft.pydroid.ui.changelog.ChangeLogActivity
import com.pyamsoft.pydroid.ui.changelog.buildChangeLog
import com.pyamsoft.pydroid.ui.theme.Theming
import com.pyamsoft.pydroid.ui.util.commit
import com.pyamsoft.pydroid.ui.util.layout
import com.pyamsoft.pydroid.ui.widget.shadow.DropshadowView
import com.pyamsoft.pydroid.util.stableLayoutHideNavigation
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

class MainActivity : ChangeLogActivity(), UiController<UnitControllerEvent> {

  private var stateSaver: StateSaver? = null

  @JvmField @Inject internal var mainFrameView: MainFrameView? = null

  @JvmField @Inject internal var toolbar: MainToolbarView? = null

  @JvmField @Inject internal var theming: Theming? = null

  @JvmField @Inject internal var factory: HomeButtonViewModelFactory? = null
  private val viewModel by fromViewModelFactory<MainViewModel> { factory?.create(this) }

  override val versionName = BuildConfig.VERSION_NAME

  override val applicationIcon = R.mipmap.ic_launcher

  override val changelog = buildChangeLog {
    feature("Support for full screen content and gesture navigation")
  }

  override val snackbarRoot: ViewGroup by lazy(NONE) {
    findViewById<CoordinatorLayout>(R.id.snackbar_root)
  }

  override val fragmentContainerId: Int
    get() = requireNotNull(mainFrameView).id()

  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(R.style.Theme_HomeButton)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.snackbar_screen)

    val layoutRoot = findViewById<ConstraintLayout>(R.id.content_root)
    Injector.obtainFromApplication<HomeButtonComponent>(this)
        .plusMain()
        .create(this, layoutRoot, this) { requireNotNull(theming).isDarkTheme(this) }
        .inject(this)

    val frameView = requireNotNull(mainFrameView)
    val toolbar = requireNotNull(toolbar)
    val dropshadow = DropshadowView.create(layoutRoot)

    stateSaver =
        createComponent(
            savedInstanceState, this, viewModel, this, frameView, toolbar, dropshadow) {}

    stableLayoutHideNavigation()

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

  override fun onControllerEvent(event: UnitControllerEvent) {}

  override fun onDestroy() {
    super.onDestroy()
    mainFrameView = null
    toolbar = null
    stateSaver = null
    factory = null
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    stateSaver?.saveState(outState)
  }

  private fun addPreferenceFragment() {
    val fm = supportFragmentManager
    if (fm.findFragmentByTag(SettingsFragment.TAG) == null) {
      fm.commit(this) { add(fragmentContainerId, SettingsFragment(), SettingsFragment.TAG) }
    }
  }
}
