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

package com.pyamsoft.homebutton.notification

import com.pyamsoft.homebutton.preference.HomeButtonPreferences
import com.pyamsoft.pydroid.core.Enforcer
import com.pyamsoft.pydroid.notify.Notifier
import com.pyamsoft.pydroid.notify.NotifyChannelInfo
import com.pyamsoft.pydroid.notify.toNotifyId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHandler @Inject internal constructor(
    @InternalApi private val notifier: Notifier,
    private val preferences: HomeButtonPreferences
) {

    @JvmOverloads
    suspend fun start(showNotification: Boolean? = null) =
        withContext(context = Dispatchers.Default) {
            Enforcer.assertOffMainThread()
            val show = showNotification ?: preferences.notificationPriority()
            notifier.cancel(ID)
            notifier.show(
                ID,
                NotifyChannelInfo(
                    id = "home_button_foreground",
                    title = "Home Service",
                    description = " Notification for the Home Button service"
                ),
                HomeNotification(show)
            )
        }

    companion object {

        private val ID = 1001.toNotifyId()
    }
}
