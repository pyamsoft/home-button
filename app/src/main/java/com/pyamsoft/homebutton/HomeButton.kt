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

package com.pyamsoft.homebutton

import android.app.Application
import com.pyamsoft.pydroid.ui.PYDroid
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher

class HomeButton : Application() {

  private var watcher: RefWatcher? = null
  private var component: HomeButtonComponent? = null

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

    PYDroid.init(
        this,
        getString(R.string.app_name),
        "https://github.com/pyamsoft/home-button",
        "https://github.com/pyamsoft/home-button/issues",
        BuildConfig.VERSION_CODE,
        BuildConfig.DEBUG
    ) {
      component = DaggerHomeButtonComponent.factory()
          .create(this)
    }
  }

  override fun getSystemService(name: String): Any? {
    val service = PYDroid.getSystemService(name)
    if (service != null) {
      return service
    }

    if (name == HomeButtonComponent::class.java.name) {
      return requireNotNull(component)
    }

    return super.getSystemService(name)
  }
}
