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

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.pyamsoft.homebutton.R
import com.pyamsoft.pydroid.notify.NotifyChannelInfo
import com.pyamsoft.pydroid.notify.NotifyData
import com.pyamsoft.pydroid.notify.NotifyDispatcher
import com.pyamsoft.pydroid.notify.NotifyId
import javax.inject.Inject
import javax.inject.Singleton
import timber.log.Timber

@Singleton
internal class HomeNotificationDispatcher
@Inject
internal constructor(private val context: Context) : NotifyDispatcher<HomeNotification> {

  private val text = context.getString(R.string.press_to_home)
  private val notificationManager by lazy { NotificationManagerCompat.from(context) }
  private val pendingIntent by lazy {
    PendingIntent.getActivity(context, RC, HOME, PendingIntent.FLAG_UPDATE_CURRENT)
  }

  private fun setupNotificationChannel(
      channelInfo: NotifyChannelInfo,
      notification: HomeNotification
  ) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
      Timber.d("No channel below Android O")
      return
    }

    val importance =
        if (notification.show) {
          NotificationManager.IMPORTANCE_DEFAULT
        } else {
          NotificationManager.IMPORTANCE_MIN
        }

    val notificationGroup = NotificationChannelGroup(channelInfo.id, channelInfo.title)
    val notificationChannel =
        NotificationChannel(channelInfo.id, channelInfo.title, importance).apply {
          group = notificationGroup.id
          lockscreenVisibility = Notification.VISIBILITY_PUBLIC
          description = channelInfo.description
          enableLights(false)
          enableVibration(false)
          setSound(null, null)
          setShowBadge(false)
        }

    Timber.d(
        "Create notification channel and group ${notificationChannel.id} ${notificationGroup.id}")
    notificationManager.apply {
      createNotificationChannelGroup(notificationGroup)
      createNotificationChannel(notificationChannel)
    }
  }

  override fun build(
      id: NotifyId,
      channelInfo: NotifyChannelInfo,
      notification: HomeNotification
  ): Notification {
    val priority =
        if (notification.show) {
          NotificationCompat.PRIORITY_DEFAULT
        } else {
          NotificationCompat.PRIORITY_MIN
        }

    setupNotificationChannel(channelInfo, notification)
    return NotificationCompat.Builder(context, channelInfo.id)
        .setAutoCancel(false)
        .setOngoing(true)
        .setContentIntent(pendingIntent)
        .setSmallIcon(R.drawable.ic_home_notification)
        .setOngoing(true)
        .setWhen(0)
        .setNumber(0)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setContentText(text)
        .setPriority(priority)
        .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
        .build()
  }

  override fun canShow(notification: NotifyData): Boolean {
    return notification is HomeNotification
  }

  companion object {

    private const val RC = 1004
    private val HOME = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME)
  }
}
