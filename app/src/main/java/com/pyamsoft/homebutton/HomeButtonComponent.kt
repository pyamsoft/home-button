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
import android.content.Context
import androidx.annotation.CheckResult
import com.pyamsoft.homebutton.main.MainComponent
import com.pyamsoft.homebutton.notification.NotificationModule
import com.pyamsoft.homebutton.receiver.BootCompletedReceiver
import com.pyamsoft.homebutton.settings.SettingsComponent
import com.pyamsoft.pydroid.ui.theme.Theming
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [HomeButtonComponent.BaseModule::class, NotificationModule::class])
internal interface HomeButtonComponent {

  fun inject(receiver: BootCompletedReceiver)

  @CheckResult fun plusMain(): MainComponent.Factory

  @CheckResult fun plusSettings(): SettingsComponent.Factory

  @Component.Factory
  interface Factory {

    fun create(
        @BindsInstance application: Application,
        @BindsInstance theming: Theming,
    ): HomeButtonComponent
  }

  @Module
  abstract class BaseModule {

    @Module
    companion object {

      @Provides
      @JvmStatic
      fun provideContext(application: Application): Context {
        return application
      }
    }
  }
}
