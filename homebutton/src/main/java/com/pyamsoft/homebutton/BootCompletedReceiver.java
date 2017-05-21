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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import timber.log.Timber;

public class BootCompletedReceiver extends BroadcastReceiver {

  @Override public final void onReceive(final Context context, final Intent intent) {
    if (null != intent) {
      final String action = intent.getAction();
      if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
        Timber.d("Home Button has started via boot receiver");
      }
    }
  }
}
