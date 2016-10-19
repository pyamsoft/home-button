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

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.CheckResult;
import timber.log.Timber;

public class BootReceiver extends BroadcastReceiver {

  public static void setBootEnabled(final Context c, final boolean bootEnabled) {
    // Because the application context is a singleton, we can init a static component name
    // using it for the two boot related functions
    final Context context = c.getApplicationContext();
    final ComponentName cmp = new ComponentName(context, BootReceiver.class);
    final int componentState = bootEnabled ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
        : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
    context.getPackageManager()
        .setComponentEnabledSetting(cmp, componentState, PackageManager.DONT_KILL_APP);
  }

  @CheckResult public static boolean isBootEnabled(final Context c) {
    // Because the application context is a singleton, we can init a static component name
    // using it for the two boot related functions
    final Context context = c.getApplicationContext();
    final ComponentName cmp = new ComponentName(context, BootReceiver.class);
    final int componentState = context.getPackageManager().getComponentEnabledSetting(cmp);
    return componentState == PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
  }

  @Override public final void onReceive(final Context context, final Intent intent) {
    if (null != intent) {
      final String action = intent.getAction();
      if (action != null && action.equals(Intent.ACTION_BOOT_COMPLETED)) {
        Timber.d("Home Button has started via boot receiver");
      }
    }
  }
}
