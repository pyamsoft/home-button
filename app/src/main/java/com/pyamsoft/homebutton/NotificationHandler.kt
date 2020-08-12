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

package com.pyamsoft.homebutton

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.pyamsoft.pydroid.core.Enforcer
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

// TODO(Peter): Convert to pydroid-notify
@Singleton
class NotificationHandler @Inject internal constructor(
    private val context: Context,
    private val preferences: HomeButtonPreferences
) {

    private val text = context.getString(R.string.press_to_home)
    private val notificationManagerCompat by lazy { NotificationManagerCompat.from(context) }
    private val pendingIntent by lazy {
        PendingIntent.getActivity(context, RC, HOME, PendingIntent.FLAG_UPDATE_CURRENT)
    }
    private val notificationManager by lazy {
        Enforcer.assertOffMainThread()
        requireNotNull(context.getSystemService<NotificationManager>())
    }

    @JvmOverloads
    suspend fun start(showNotification: Boolean? = null) =
        withContext(context = Dispatchers.Default) {
            Enforcer.assertOffMainThread()
            val show = showNotification ?: preferences.notificationPriority()
            val notificationChannelId = setupNotificationChannel(show)

            val priority =
                if (show) NotificationCompat.PRIORITY_DEFAULT else NotificationCompat.PRIORITY_MIN

            val builder = NotificationCompat.Builder(context, notificationChannelId)
                .apply {
                    setContentIntent(pendingIntent)
                    setSmallIcon(R.drawable.ic_home_notification)
                    setOngoing(true)
                    setWhen(0)
                    setNumber(0)
                    setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    setContentText(text)
                    color = ContextCompat.getColor(context, R.color.colorPrimary)
                }

            notificationManagerCompat.cancel(ID)
            notificationManagerCompat.notify(ID, builder.setPriority(priority).build())
        }

    private suspend fun setupNotificationChannel(showNotification: Boolean): String {
        Enforcer.assertOffMainThread()
        val name = "Home Service"
        val desc = "Notification related to the Home Button service"

        var channelId = preferences.notificationChannelId()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return channelId
        } else {
            val importance = if (showNotification) {
                NotificationManager.IMPORTANCE_DEFAULT
            } else {
                NotificationManager.IMPORTANCE_MIN
            }

            // Delete the old notification if it exists, we don't use this ID anymore
            if (notificationManager.getNotificationChannel(OLD_CHANNEL_ID) != null) {
                notificationManager.deleteNotificationChannel(OLD_CHANNEL_ID)
            }

            // If the notification channel exists, delete it so we can create a new one
            if (notificationManager.getNotificationChannel(channelId) != null) {
                notificationManager.deleteNotificationChannel(channelId)
                preferences.clearNotificationChannel()
                channelId = preferences.notificationChannelId()
            }

            val notificationChannel =
                NotificationChannel(channelId, name, importance).apply {
                    lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                    description = desc
                    enableLights(false)
                    enableVibration(false)
                    setSound(null, null)
                    setShowBadge(false)
                }

            Timber.d("Create notification channel with id: %s", notificationChannel.id)
            notificationManager.createNotificationChannel(notificationChannel)

            return channelId
        }
    }

    companion object {

        private const val ID = 1001
        private const val RC = 1004
        private val HOME = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME)

        private const val OLD_CHANNEL_ID = "home_button_foreground"
    }
}
