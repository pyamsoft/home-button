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

import android.os.Bundle
import com.pyamsoft.homebutton.R
import com.pyamsoft.pydroid.arch.UiView
import com.pyamsoft.pydroid.ui.app.ToolbarActivity
import com.pyamsoft.pydroid.ui.arch.InvalidIdException
import com.pyamsoft.pydroid.ui.util.DebouncedOnClickListener
import com.pyamsoft.pydroid.ui.util.setUpEnabled

internal class ToolbarView internal constructor(
  private val toolbarActivity: ToolbarActivity,
  private val callback: Callback
) : UiView {

  private var oldTitle: CharSequence? = null

  override fun id(): Int {
    throw InvalidIdException
  }

  override fun inflate(savedInstanceState: Bundle?) {
    toolbarActivity.withToolbar { toolbar ->
      toolbar.setTitle(R.string.app_name)
      toolbar.setUpEnabled(false)
      toolbar.setNavigationOnClickListener(DebouncedOnClickListener.create {
        callback.onToolbarNavClicked()
      })
    }
  }

  override fun teardown() {
    toolbarActivity.withToolbar { toolbar ->
      toolbar.setNavigationOnClickListener(null)
    }
  }

  override fun saveState(outState: Bundle) {
    outState.putCharSequence(KEY_OLD_TITLE, oldTitle)
  }

  interface Callback {

    fun onToolbarNavClicked()

  }

  companion object {

    private const val KEY_OLD_TITLE = "key_old_title"
  }

}
