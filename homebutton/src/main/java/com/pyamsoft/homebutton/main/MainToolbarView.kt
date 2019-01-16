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
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import com.pyamsoft.homebutton.R.string
import com.pyamsoft.homebutton.databinding.MainToolbarBinding
import com.pyamsoft.homebutton.main.MainViewEvent.ToolbarClicked
import com.pyamsoft.pydroid.core.bus.Publisher
import com.pyamsoft.pydroid.ui.app.activity.ActivityBase
import com.pyamsoft.pydroid.ui.arch.UiView
import com.pyamsoft.pydroid.ui.util.DebouncedOnClickListener
import com.pyamsoft.pydroid.util.toDp

internal class MainToolbarView internal constructor(
  private val parent: ViewGroup,
  private val activity: ActivityBase,
  uiBus: Publisher<MainViewEvent>
) : UiView<MainViewEvent>(uiBus) {

  private lateinit var binding: MainToolbarBinding

  override fun id(): Int {
    return binding.mainToolbar.id
  }

  override fun inflate(savedInstanceState: Bundle?) {
    binding = MainToolbarBinding.inflate(parent.inflater(), parent, true)

    setupToolbar()
  }

  override fun teardown() {
    binding.unbind()
  }

  override fun saveState(outState: Bundle) {
  }

  private fun setupToolbar() {
    binding.mainToolbar.apply {
      activity.setToolbar(this)
      setTitle(string.app_name)
      ViewCompat.setElevation(this, 0.toDp(context).toFloat())

      setNavigationOnClickListener(DebouncedOnClickListener.create {
        publish(ToolbarClicked)
      })
    }
  }

}

