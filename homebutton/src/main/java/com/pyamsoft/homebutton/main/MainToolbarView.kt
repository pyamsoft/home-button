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
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import com.pyamsoft.homebutton.R
import com.pyamsoft.homebutton.R.string
import com.pyamsoft.pydroid.arch.BaseUiView
import com.pyamsoft.pydroid.ui.app.ToolbarActivityProvider
import com.pyamsoft.pydroid.util.toDp
import javax.inject.Inject

internal class MainToolbarView @Inject internal constructor(
  parent: ViewGroup,
  private val toolbarProvider: ToolbarActivityProvider
) : BaseUiView<Unit>(parent, Unit) {

  override val layout: Int = R.layout.main_toolbar

  override val layoutRoot by boundView<Toolbar>(R.id.main_toolbar)

  override fun onInflated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    layoutRoot.apply {
      toolbarProvider.setToolbar(this)
      setTitle(string.app_name)
      ViewCompat.setElevation(this, 0.toDp(context).toFloat())
    }
  }

  override fun onTeardown() {
    toolbarProvider.setToolbar(null)
  }

}

