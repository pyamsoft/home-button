/*
 * Copyright 2013 - 2016 Peter Kenji Yamanaka
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
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.pyamsoft.pydroid.base.ActivityBase;
import java.util.Locale;

public final class MainActivity extends ActivityBase {

  @BindView(R.id.boot_icon) ImageView image;
  @BindView(R.id.boot_enabled) SwitchCompat sw;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.version) TextView version;
  @BindView(R.id.build) TextView build;
  private Unbinder unbinder;

  @Override protected final void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    unbinder = ButterKnife.bind(this);

    setupToolbar();
    setupContent();
    setupBuildAndVersion();
  }

  // Display basic version information
  private void setupBuildAndVersion() {
    version.setText(BuildConfig.VERSION_NAME);
    build.setText(String.format(Locale.US, "%d", BuildConfig.VERSION_CODE));
  }

  private void setupContent() {
    sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
      BootActionReceiver.setBootEnabled(buttonView.getContext(), isChecked);
      setImageState();
    });
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (unbinder != null) {
      unbinder.unbind();
    }
  }

  private void setupToolbar() {
    toolbar.setTitle(getString(R.string.app_name));
    setSupportActionBar(toolbar);
  }

  private void setImageState() {
    if (image != null) {
      image.setEnabled(BootActionReceiver.isBootEnabled(this));
    }
  }

  @Override protected void onPostResume() {
    super.onPostResume();
    setImageState();
    setEnabledState();
    if (!BillingProcessor.isIabServiceAvailable(this)) {
      showDonationUnavailableDialog();
    }
  }

  private void setEnabledState() {
    if (sw != null) {
      sw.setChecked(BootActionReceiver.isBootEnabled(this));
    }
  }

  @Override protected String getPlayStoreAppPackage() {
    return getPackageName();
  }
}
