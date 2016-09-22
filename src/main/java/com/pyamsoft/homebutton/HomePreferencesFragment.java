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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.preference.Preference;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.view.View;
import com.pyamsoft.pydroid.about.AboutLibrariesFragment;
import com.pyamsoft.pydroid.app.fragment.ActionBarSettingsPreferenceFragment;
import com.pyamsoft.pydroid.about.Licenses;

public class HomePreferencesFragment extends ActionBarSettingsPreferenceFragment {

  @NonNull public static final String TAG = "HomePreferencesFragment";

  @NonNull @Override protected AboutLibrariesFragment.BackStackState isLastOnBackStack() {
    return AboutLibrariesFragment.BackStackState.LAST;
  }

  @Override public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    addPreferencesFromResource(R.xml.preferences);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    final Preference upgradeInfo = findPreference(getString(R.string.upgrade_info_key));
    upgradeInfo.setOnPreferenceClickListener(preference -> showChangelog());

    final SwitchPreferenceCompat showAds =
        (SwitchPreferenceCompat) findPreference(getString(R.string.adview_key));
    showAds.setOnPreferenceChangeListener((preference, newValue) -> toggleAdVisibility(newValue));

    final SwitchPreferenceCompat startBoot =
        (SwitchPreferenceCompat) findPreference(getString(R.string.boot_key));
    startBoot.setChecked(BootReceiver.isBootEnabled(getContext()));
    startBoot.setOnPreferenceClickListener(preference -> {
      final boolean currentState = BootReceiver.isBootEnabled(getContext());
      BootReceiver.setBootEnabled(getContext(), !currentState);
      return true;
    });

    final Preference showAboutLicenses = findPreference(getString(R.string.about_license_key));
    showAboutLicenses.setOnPreferenceClickListener(
        preference -> showAboutLicensesFragment(R.id.main_view_container,
            AboutLibrariesFragment.Styling.LIGHT, Licenses.Id.ANDROID, Licenses.Id.ANDROID_SUPPORT,
            Licenses.Id.PYDROID, Licenses.Id.GOOGLE_PLAY_SERVICES, Licenses.Id.ANDROID_CHECKOUT,
            Licenses.Id.BUTTERKNIFE, Licenses.Id.DAGGER, Licenses.Id.FAST_ADAPTER,
            Licenses.Id.FIREBASE, Licenses.Id.LEAK_CANARY, Licenses.Id.RETROFIT2,
            Licenses.Id.RXANDROID, Licenses.Id.RXJAVA));

    final Preference checkVersion = findPreference(getString(R.string.check_version_key));
    checkVersion.setOnPreferenceClickListener(preference -> checkForUpdate());
  }
}
