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

package com.pyamsoft.homebutton

import android.app.Application
import androidx.annotation.CheckResult
import com.pyamsoft.pydroid.bootstrap.libraries.OssLibraries
import com.pyamsoft.pydroid.ui.PYDroid

class HomeButton : Application() {

  private val component by lazy {
    val url = "https://github.com/pyamsoft/home-button"
    val provider =
        PYDroid.init(
            this,
            PYDroid.Parameters(
                viewSourceUrl = url,
                bugReportUrl = "$url/issues",
                privacyPolicyUrl = PRIVACY_POLICY_URL,
                termsConditionsUrl = TERMS_CONDITIONS_URL,
                version = BuildConfig.VERSION_CODE))

    // Using pydroid-notify
    OssLibraries.usingNotify = true

    // Using pydroid-autopsy
    OssLibraries.usingAutopsy = true

    return@lazy DaggerHomeButtonComponent.factory().create(this, provider.get().theming())
  }

  override fun getSystemService(name: String): Any? {
    // Use component here in a weird way to guarantee the lazy is initialized.
    return component.run { PYDroid.getSystemService(name) } ?: fallbackGetSystemService(name)
  }

  @CheckResult
  private fun fallbackGetSystemService(name: String): Any? {
    return if (name == HomeButtonComponent::class.java.name) component
    else {
      super.getSystemService(name)
    }
  }

  companion object {
    const val PRIVACY_POLICY_URL = "https://pyamsoft.blogspot.com/p/home-button-privacy-policy.html"
    const val TERMS_CONDITIONS_URL =
        "https://pyamsoft.blogspot.com/p/home-button-terms-and-conditions.html"
  }
}
