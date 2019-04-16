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

package com.pyamsoft.homebutton.settings

import androidx.annotation.CheckResult
import androidx.preference.PreferenceScreen
import com.pyamsoft.homebutton.settings.SettingsComponent.SettingsModule
import com.pyamsoft.homebutton.settings.SettingsHandler.SettingsEvent
import com.pyamsoft.pydroid.arch.UiEventHandler
import com.pyamsoft.pydroid.ui.app.ToolbarActivity
import dagger.Binds
import dagger.BindsInstance
import dagger.Module
import dagger.Subcomponent

@Subcomponent(modules = [SettingsModule::class])
internal interface SettingsComponent {

  fun inject(fragment: HomePreferencesFragment)

  @Subcomponent.Factory
  interface Factory {

    @CheckResult
    fun create(
      @BindsInstance toolbarActivity: ToolbarActivity,
      @BindsInstance preferenceScreen: PreferenceScreen
    ): SettingsComponent

  }

  @Module
  abstract class SettingsModule {

    @Binds
    @CheckResult
    internal abstract fun bindUiComponent(impl: SettingsUiComponentImpl): SettingsUiComponent

    @Binds
    @CheckResult
    internal abstract fun bindUiCallback(impl: SettingsHandler): SettingsView.Callback

    @Binds
    @CheckResult
    internal abstract fun bindUiHandler(impl: SettingsHandler): UiEventHandler<SettingsEvent, SettingsView.Callback>
  }

}