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

import android.app.Application
import android.content.Context
import androidx.annotation.CheckResult
import com.pyamsoft.pydroid.ui.PYDroid
import com.pyamsoft.pydroid.ui.theme.Theming
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import kotlin.LazyThreadSafetyMode.NONE

class HomeButton : Application(), PYDroid.Instance {

  private var pyDroid: PYDroid? = null
  private val preferences by lazy(NONE) { HomeButtonPreferences(this) }
  private val notificationHandler by lazy(NONE) { NotificationHandler(this, preferences) }

  private lateinit var watcher: RefWatcher
  private lateinit var theming: Theming

  override fun onCreate() {
    super.onCreate()
    if (LeakCanary.isInAnalyzerProcess(this)) {
      return
    }

    if (BuildConfig.DEBUG) {
      // Assign
      watcher = LeakCanary.install(this)
    } else {
      // Assign
      watcher = RefWatcher.DISABLED
    }
    notificationHandler.start()

    Theming.IS_DEFAULT_DARK_THEME = false
    PYDroid.init(
        instance = this,
        application = this,
        applicationName = getString(R.string.app_name),
        bugreportUrl = "https://github.com/pyamsoft/home-button/issues",
        currentVersion = BuildConfig.VERSION_CODE,
        debug = BuildConfig.DEBUG
    )
  }

  override fun getPydroid(): PYDroid? = pyDroid

  override fun setPydroid(instance: PYDroid) {
    pyDroid = instance.also {
      theming = it.modules()
          .theming()
    }
  }

  companion object {

    @JvmStatic
    @CheckResult
    fun notificationHandler(context: Context): NotificationHandler {
      return (context.applicationContext as? HomeButton)?.notificationHandler
          ?: throw IllegalStateException("Cannot access NotificationHandler from Application")

    }

    @JvmStatic
    @CheckResult
    fun theming(context: Context): Theming {
      return (context.applicationContext as? HomeButton)?.theming
          ?: throw IllegalStateException("Cannot access Theming from Application")

    }
  }
}
