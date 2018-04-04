/*
 * Copyright (C) 2018 Peter Kenji Yamanaka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pyamsoft.homebutton

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import timber.log.Timber
import java.util.UUID

class NotificationHandler(
  private val context: Context,
  private val preferences: HomeButtonPreferences
) {

  private val notificationManager =
    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
  private val notificationManagerCompat = NotificationManagerCompat.from(context)
  private val title = context.getString(R.string.app_name)
  private val text = context.getString(R.string.press_to_home)
  private val pendingIntent: PendingIntent

  init {
    pendingIntent =
        PendingIntent.getActivity(context, RC, HOME, PendingIntent.FLAG_UPDATE_CURRENT)
  }

  fun start() {
    val showNotification: Boolean = preferences.notificationPriority
    start(showNotification)
  }

  fun start(showNotification: Boolean) {
    val notificationChannelId = setupNotificationChannel(showNotification)

    val priority: Int
    if (showNotification) {
      priority = NotificationCompat.PRIORITY_DEFAULT
    } else {
      priority = NotificationCompat.PRIORITY_MIN
    }

    val builder = NotificationCompat.Builder(context, notificationChannelId)
        .apply {
          setContentIntent(pendingIntent)
          setSmallIcon(R.drawable.ic_home_notification)
          setOngoing(true)
          setWhen(0)
          setNumber(0)
          setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
          setContentTitle(title)
          setContentText(text)
          color = ContextCompat.getColor(context, R.color.primary)
        }

    notificationManagerCompat.notify(ID, builder.setPriority(priority).build())
  }

  private fun setupNotificationChannel(showNotification: Boolean): String {
    val name = "Home Service"
    val desc = "Notification related to the Home Button service"

    val channelId = UUID.randomUUID()
        .toString()
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
      return channelId
    } else {
      val importance: Int
      if (showNotification) {
        importance = NotificationManager.IMPORTANCE_DEFAULT
      } else {
        importance = NotificationManager.IMPORTANCE_MIN
      }

      val notificationChannel =
        NotificationChannel(channelId, name, importance).apply {
          lockscreenVisibility = Notification.VISIBILITY_PUBLIC
          description = desc
          group = GROUP_ID
          enableLights(false)
          enableVibration(false)
          setSound(null, null)
          setShowBadge(false)
        }

      // Delete the old notification if it exists
      if (notificationManager.getNotificationChannel(OLD_CHANNEL_ID) != null) {
        notificationManager.deleteNotificationChannel(OLD_CHANNEL_ID)
      }

      // Delete any old notification groups
      val notificationGroups = notificationManager.notificationChannelGroups ?: emptyList()
      for (group in notificationGroups.asSequence()) {
        notificationManager.deleteNotificationChannelGroup(group.id)
      }

      Timber.d("Create notification channel with id: %s", notificationChannel.id)
      notificationManager.createNotificationChannelGroup(NotificationChannelGroup(GROUP_ID, title))
      notificationManager.createNotificationChannel(notificationChannel)

      return channelId
    }

  }

  companion object {

    private const val ID = 1001
    private const val RC = 1004
    private val HOME = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME)

    private const val OLD_CHANNEL_ID = "home_button_foreground"
    private const val GROUP_ID = "com.pyamsoft.homebutton"
  }

}
