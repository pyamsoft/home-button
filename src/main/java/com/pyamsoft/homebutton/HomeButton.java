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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;
import com.pyamsoft.pydroid.base.ApplicationBase;
import com.squareup.leakcanary.LeakCanary;

public class HomeButton extends ApplicationBase {

  final static int ID = 1001;
  final static int RC = 1004;

  // The application is simple, so we don't really add options to enable or disable a notification
  static void startHomeNotification(final Context c) {
    final Intent home = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME);
    final Context context = c.getApplicationContext();
    final PendingIntent pe =
        PendingIntent.getActivity(context, RC, home, PendingIntent.FLAG_UPDATE_CURRENT);
    final Notification n = new NotificationCompat.Builder(context).setContentIntent(pe)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setOngoing(true)
        .setWhen(0)
        .setNumber(0)
        .setPriority(NotificationCompat.PRIORITY_MIN)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setColor(ContextCompat.getColor(context, R.color.primary))
        .setContentTitle(context.getResources().getString(R.string.app_name))
        .setContentText(context.getString(R.string.press_to_home))
        .build();
    final NotificationManager notificationManager =
        (NotificationManager) context.getApplicationContext()
            .getSystemService(Context.NOTIFICATION_SERVICE);
    notificationManager.notify(ID, n);
  }

  @Override protected void installInDebugMode() {
    super.installInDebugMode();
    LeakCanary.install(this);
  }

  @Override protected void installInNonTestMode() {
    super.installInNonTestMode();
    startHomeNotification(this);
    Toast.makeText(this, getString(R.string.home_button_started), Toast.LENGTH_SHORT).show();
  }
}
