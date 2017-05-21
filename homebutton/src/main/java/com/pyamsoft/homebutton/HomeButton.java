/*
 * Copyright 2017 Peter Kenji Yamanaka
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

import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;
import com.pyamsoft.pydroid.about.Licenses;
import com.pyamsoft.pydroid.helper.Checker;
import com.pyamsoft.pydroid.ui.PYDroid;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class HomeButton extends Application {

  @NonNull private final Intent home =
      new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME);
  @Nullable private RefWatcher refWatcher;

  @CheckResult @NonNull static RefWatcher getRefWatcher(@NonNull Fragment fragment) {
    final Application application = Checker.checkNonNull(fragment).getActivity().getApplication();
    if (application instanceof HomeButton) {
      return ((HomeButton) application).getWatcher();
    } else {
      throw new IllegalStateException("Application is not Home Button");
    }
  }

  @NonNull @CheckResult private RefWatcher getWatcher() {
    return Checker.checkNonNull(refWatcher);
  }

  @Override public void onCreate() {
    super.onCreate();
    if (LeakCanary.isInAnalyzerProcess(this)) {
      return;
    }
    PYDroid.initialize(this, BuildConfig.DEBUG);
    if (BuildConfig.DEBUG) {
      refWatcher = LeakCanary.install(this);
    } else {
      refWatcher = RefWatcher.DISABLED;
    }

    Licenses.create("Firebase", "https://firebase.google.com", "licenses/firebase");
    startHomeNotification();

    Toast.makeText(getApplicationContext(), getString(R.string.home_button_started),
        Toast.LENGTH_SHORT).show();
  }

  // The application is simple, so we don't really add options to enable or disable a notification
  private void startHomeNotification() {
    int ID = 1001;
    int RC = 1004;

    NotificationManagerCompat notificationManager =
        NotificationManagerCompat.from(getApplicationContext());

    PendingIntent pe = PendingIntent.getActivity(getApplicationContext(), RC, home,
        PendingIntent.FLAG_UPDATE_CURRENT);
    Notification n = new NotificationCompat.Builder(getApplicationContext()).setContentIntent(pe)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setOngoing(true)
        .setWhen(0)
        .setNumber(0)
        .setPriority(NotificationCompat.PRIORITY_MIN)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setColor(ContextCompat.getColor(getApplicationContext(), R.color.primary))
        .setContentTitle(getString(R.string.app_name))
        .setContentText(getString(R.string.press_to_home))
        .build();

    notificationManager.notify(ID, n);
  }
}
