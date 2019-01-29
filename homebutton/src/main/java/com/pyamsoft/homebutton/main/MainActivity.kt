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
import com.pyamsoft.homebutton.BuildConfig
import com.pyamsoft.homebutton.HomeButton
import com.pyamsoft.homebutton.R
import com.pyamsoft.homebutton.R.mipmap
import com.pyamsoft.homebutton.R.style
import com.pyamsoft.homebutton.main.MainViewEvent.ToolbarClicked
import com.pyamsoft.homebutton.settings.HomeFragment
import com.pyamsoft.pydroid.core.bus.RxBus
import com.pyamsoft.pydroid.ui.about.AboutFragment
import com.pyamsoft.pydroid.ui.arch.destroy
import com.pyamsoft.pydroid.ui.rating.ChangeLogBuilder
import com.pyamsoft.pydroid.ui.rating.RatingActivity
import com.pyamsoft.pydroid.ui.rating.buildChangeLog
import com.pyamsoft.pydroid.ui.util.commit
import com.pyamsoft.pydroid.ui.widget.shadow.DropshadowUiComponent

class MainActivity : RatingActivity() {

  private val bus = RxBus.create<MainViewEvent>()
  private lateinit var dropshadowComponent: DropshadowUiComponent
  private lateinit var toolbarComponent: MainToolbarUiComponent
  private lateinit var frameComponent: MainFrameUiComponent

  private lateinit var layoutRoot: ConstraintLayout

  override val versionName: String = BuildConfig.VERSION_NAME

  override val applicationIcon: Int = mipmap.ic_launcher

  override val snackbarRoot: View
    get() = layoutRoot

  override val fragmentContainerId: Int
    get() = frameComponent.id()

  override val changeLogLines: ChangeLogBuilder = buildChangeLog {
    change("New icon style")
    change("Better open source license viewing experience")
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    if (HomeButton.theming(this).isDarkTheme()) {
      setTheme(style.Theme_HomeButton_Dark)
    } else {
      setTheme(style.Theme_HomeButton_Light)
    }
    super.onCreate(savedInstanceState)
    setContentView(R.layout.layout_constraint)
    layoutRoot = findViewById(R.id.layout_constraint)

    inflateLayout()
    listenForViewEvents()
    createComponents(savedInstanceState)
    layoutConstraints()
    addPreferenceFragment()
  }

  private fun layoutConstraints() {
    ConstraintSet().apply {
      clone(layoutRoot)

      toolbarComponent.also {
        connect(it.id(), ConstraintSet.TOP, layoutRoot.id, ConstraintSet.TOP)
        connect(it.id(), ConstraintSet.START, layoutRoot.id, ConstraintSet.START)
        connect(it.id(), ConstraintSet.END, layoutRoot.id, ConstraintSet.END)
        constrainWidth(it.id(), ConstraintSet.MATCH_CONSTRAINT)
      }

      frameComponent.also {
        connect(it.id(), ConstraintSet.TOP, toolbarComponent.id(), ConstraintSet.BOTTOM)
        connect(it.id(), ConstraintSet.BOTTOM, layoutRoot.id, ConstraintSet.BOTTOM)
        connect(it.id(), ConstraintSet.START, layoutRoot.id, ConstraintSet.START)
        connect(it.id(), ConstraintSet.END, layoutRoot.id, ConstraintSet.END)
        constrainHeight(it.id(), ConstraintSet.MATCH_CONSTRAINT)
        constrainWidth(it.id(), ConstraintSet.MATCH_CONSTRAINT)
      }

      dropshadowComponent.also {
        connect(it.id(), ConstraintSet.TOP, toolbarComponent.id(), ConstraintSet.BOTTOM)
        connect(it.id(), ConstraintSet.START, layoutRoot.id, ConstraintSet.START)
        connect(it.id(), ConstraintSet.END, layoutRoot.id, ConstraintSet.END)
        constrainWidth(it.id(), ConstraintSet.MATCH_CONSTRAINT)
      }

      applyTo(layoutRoot)
    }
  }

  private fun inflateLayout() {
    val toolbar = MainToolbarView(this, layoutRoot, bus)
    val frame = MainFrameView(layoutRoot)

    toolbarComponent = MainToolbarUiComponent(toolbar, this)
    frameComponent = MainFrameUiComponent(frame, this)
    dropshadowComponent = DropshadowUiComponent.create(layoutRoot, this)
  }

  private fun listenForViewEvents() {
    toolbarComponent.onUiEvent {
      return@onUiEvent when (it) {
        ToolbarClicked -> onBackPressed()
      }
    }
        .destroy(this)
  }

  private fun createComponents(savedInstanceState: Bundle?) {
    toolbarComponent.create(savedInstanceState)
    frameComponent.create(savedInstanceState)
    dropshadowComponent.create(savedInstanceState)
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    toolbarComponent.saveState(outState)
    frameComponent.saveState(outState)
    dropshadowComponent.saveState(outState)
  }

  private fun addPreferenceFragment() {
    val fm = supportFragmentManager
    if (fm.findFragmentByTag(HomeFragment.TAG) == null && !AboutFragment.isPresent(this)) {
      fm.beginTransaction()
          .add(frameComponent.id(), HomeFragment(), HomeFragment.TAG)
          .commit(this)
    }
  }
}