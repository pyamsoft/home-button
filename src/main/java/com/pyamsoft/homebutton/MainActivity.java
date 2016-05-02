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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.pyamsoft.pydroid.base.ActivityBase;
import timber.log.Timber;

public final class MainActivity extends ActivityBase implements BillingProcessor.IBillingHandler {

  private BillingProcessor billingProcessor;

  @BindView(R.id.boot_icon) ImageView image;
  @BindView(R.id.boot_enabled) SwitchCompat sw;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.version) TextView version;
  @BindView(R.id.build) TextView build;
  private Unbinder unbinder;

  @Override protected final void onCreate(final Bundle savedInstanceState) {
    setupFakeFullscreenWindow();
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    billingProcessor = new BillingProcessor(this, getPackageName(), this);
    unbinder = ButterKnife.bind(this);

    setupToolbar();
    setupContent();
    setupBuildAndVersion();
    Toast.makeText(this, getString(R.string.home_button_started), Toast.LENGTH_SHORT).show();
  }

  // Display basic version information
  private void setupBuildAndVersion() {
    version.setText(BuildConfig.VERSION_NAME);
    build.setText(String.format("%d", BuildConfig.VERSION_CODE));
  }

  private void setupContent() {
    sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
      BootActionReceiver.setBootEnabled(buttonView.getContext(), isChecked);
      setImageState();
    });
  }

  @Override protected void onDestroy() {
    if (billingProcessor != null) {
      billingProcessor.release();
      billingProcessor = null;
    }
    if (unbinder != null) {
      unbinder.unbind();
    }
    super.onDestroy();
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
    return "com.pyamsoft.homebutton";
  }

  @Override public BillingProcessor getBillingProcessor() {
    return billingProcessor;
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (billingProcessor != null) {
      if (billingProcessor.handleActivityResult(requestCode, resultCode, data)) {
        Timber.d("Handled by BillingProcessor");
        return;
      }
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  @Override public void onProductPurchased(String productId, TransactionDetails details) {
    Timber.d("Product purchased: %s", productId);
    if (billingProcessor != null) {
      billingProcessor.consumePurchase(productId);
    } else {
      Timber.e("Could not consume purchase: %s", productId);
    }
  }

  @Override public void onPurchaseHistoryRestored() {
    Timber.d("Restore purchase history");
  }

  @Override public void onBillingError(int errorCode, Throwable error) {

  }

  @Override public void onBillingInitialized() {
    Timber.d("Billing initialized, attempt to consume leftovers");
    consumeLeftOverPurchases(billingProcessor);
  }
}
