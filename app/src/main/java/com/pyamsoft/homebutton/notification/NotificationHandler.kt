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

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.pyamsoft.homebutton.preference.HomeButtonPreferences
import com.pyamsoft.pydroid.core.Enforcer
import com.pyamsoft.pydroid.notify.Notifier
import com.pyamsoft.pydroid.notify.NotifyChannelInfo
import com.pyamsoft.pydroid.notify.toNotifyId
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

@Singleton
class NotificationHandler
@Inject
internal constructor(
    @InternalApi private val notifier: Notifier,
    private val preferences: HomeButtonPreferences,
    private val context: Context,
) {

  private val notificationManager by lazy { NotificationManagerCompat.from(context) }

  suspend fun start(show: Boolean? = null) =
      withContext(context = Dispatchers.Default) {
        Enforcer.assertOffMainThread()

        val reallyShow = show ?: preferences.showNotification()
        showNotification(reallyShow)
      }

  suspend fun openSettings(activity: Activity) =
      withContext(context = Dispatchers.Default) {
        Enforcer.assertOffMainThread()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          if (notificationManager.getNotificationChannel(CHANNEL_ID) != null) {
            // Channel already exists, cannot edit it - go to settings
            openNotificationSettings(activity)
            return@withContext
          }
        }
      }

  @RequiresApi(Build.VERSION_CODES.O)
  private fun openNotificationSettings(activity: Activity) {
    val settingsIntent =
        Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
          putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
          putExtra(Settings.EXTRA_CHANNEL_ID, CHANNEL_ID)
        }
    activity.startActivity(settingsIntent)
  }

  private fun showNotification(show: Boolean) {
    notifier.cancel(ID)
    notifier.show(
            ID,
            NotifyChannelInfo(
                id = CHANNEL_ID,
                title = "Home Service",
                description = " Notification for the Home Button service"),
            HomeNotification(show))
        .also { Timber.d("Show notification with id: $ID") }
  }

  companion object {

    private const val CHANNEL_ID = "home_button_foreground"
    private val ID = 1001.toNotifyId()
  }
}
