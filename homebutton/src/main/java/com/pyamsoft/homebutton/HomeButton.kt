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
import com.pyamsoft.pydroid.loader.ImageLoader
import com.pyamsoft.pydroid.ui.PYDroid
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import kotlin.LazyThreadSafetyMode.NONE

class HomeButton : Application(), PYDroid.Instance {

  private var pyDroid: PYDroid? = null
  private val preferences by lazy(NONE) { HomeButtonPreferences(this) }
  private val notificationHandler by lazy(NONE) { NotificationHandler(this, preferences) }

  private lateinit var watcher: RefWatcher
  private lateinit var imageLoader: ImageLoader

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

    PYDroid.init(this, this, BuildConfig.DEBUG)
  }

  override fun getPydroid(): PYDroid? = pyDroid

  override fun setPydroid(instance: PYDroid) {
    pyDroid = instance.also {
      imageLoader = it.modules().loaderModule().provideImageLoader()
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
    fun imageLoader(context: Context) : ImageLoader {
      return (context.applicationContext as? HomeButton)?.imageLoader
          ?: throw IllegalStateException("Cannot access ImageLoader from Application")
    }
  }
}
