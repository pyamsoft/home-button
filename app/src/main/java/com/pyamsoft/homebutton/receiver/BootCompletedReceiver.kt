/*
 * Copyright 2020 Peter Kenji Yamanaka
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

package com.pyamsoft.homebutton.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pyamsoft.homebutton.HomeButtonComponent
import com.pyamsoft.homebutton.notification.NotificationHandler
import com.pyamsoft.pydroid.ui.Injector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class BootCompletedReceiver : BroadcastReceiver() {

    @JvmField
    @Inject
    internal var notificationHandler: NotificationHandler? = null

    override fun onReceive(
        context: Context,
        intent: Intent?
    ) {
        if (Intent.ACTION_BOOT_COMPLETED == intent?.action) {
            Timber.d("Home Button has started via boot receiver")

            Injector.obtain<HomeButtonComponent>(context.applicationContext)
                .inject(this)

            // Use GlobalScope to launch the notification handler
            GlobalScope.launch(context = Dispatchers.Default) {
                requireNotNull(notificationHandler).start()
            }
        }
    }
}
