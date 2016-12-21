/*
 * Copyright 2016 Peter Kenji Yamanaka
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

package com.pyamsoft.homebutton;

import android.support.annotation.NonNull;
import com.pyamsoft.pydroidui.about.AboutLibrariesFragment;
import com.pyamsoft.pydroidui.app.fragment.ActionBarSettingsPreferenceFragment;

public class HomePreferencesFragment extends ActionBarSettingsPreferenceFragment {

  @NonNull public static final String TAG = "HomePreferencesFragment";

  @NonNull @Override protected AboutLibrariesFragment.BackStackState isLastOnBackStack() {
    return AboutLibrariesFragment.BackStackState.LAST;
  }

  @Override protected int getRootViewContainer() {
    return R.id.main_view_container;
  }

  @NonNull @Override protected String getApplicationName() {
    return getString(R.string.app_name);
  }

  @Override protected boolean hideClearAll() {
    return true;
  }

  @Override public void onDestroy() {
    super.onDestroy();
    HomeButton.getRefWatcher(this).watch(this);
  }
}