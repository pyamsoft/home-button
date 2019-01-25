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

import androidx.lifecycle.LifecycleOwner
import com.pyamsoft.pydroid.core.bus.Listener
import com.pyamsoft.pydroid.ui.arch.UiComponent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

internal class MainToolbarUiComponent internal constructor(
  view: MainToolbarView,
  uiBus: Listener<MainViewEvent>,
  owner: LifecycleOwner
) : UiComponent<MainViewEvent, MainToolbarView>(view, uiBus, owner) {

  override fun onUiEvent(): Observable<MainViewEvent> {
    return super.onUiEvent()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
  }

}
