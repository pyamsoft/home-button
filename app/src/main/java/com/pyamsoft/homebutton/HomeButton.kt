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

class HomeButton : Application() {

    private var component: HomeButtonComponent? = null

    override fun onCreate() {
        super.onCreate()
        PYDroid.init(
            this,
            getString(R.string.app_name),
            "https://github.com/pyamsoft/home-button",
            "https://github.com/pyamsoft/home-button/issues",
            PRIVACY_POLICY_URL,
            TERMS_CONDITIONS_URL,
            BuildConfig.VERSION_CODE,
            BuildConfig.DEBUG
        ) { provider ->
            component = DaggerHomeButtonComponent.factory()
                .create(this, provider.theming())
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

    companion object {
        const val PRIVACY_POLICY_URL =
            "https://pyamsoft.blogspot.com/p/home-button-privacy-policy.html"
        const val TERMS_CONDITIONS_URL =
            "https://pyamsoft.blogspot.com/p/home-button-terms-and-conditions.html"
    }
}