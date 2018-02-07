/*
 *     Copyright (C) 2017 Peter Kenji Yamanaka
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.pyamsoft.homebutton

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION_CODES
import android.support.annotation.CheckResult
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import com.pyamsoft.pydroid.PYDroidModule
import com.pyamsoft.pydroid.PYDroidModuleImpl
import com.pyamsoft.pydroid.base.about.Licenses
import com.pyamsoft.pydroid.loader.LoaderModule
import com.pyamsoft.pydroid.loader.LoaderModuleImpl
import com.pyamsoft.pydroid.ui.PYDroid
import com.pyamsoft.pydroid.util.Toasty
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import timber.log.Timber

class HomeButton : Application() {

  private val home = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME)
  @get:CheckResult
  private lateinit var watcher: RefWatcher

  override fun onCreate() {
    super.onCreate()
    if (LeakCanary.isInAnalyzerProcess(this)) {
      return
    }

    val pydroidModule: PYDroidModule = PYDroidModuleImpl(this, BuildConfig.DEBUG)
    val loaderModule: LoaderModule = LoaderModuleImpl(pydroidModule)
    PYDroid.init(pydroidModule, loaderModule)
    Licenses.create("Firebase", "https://firebase.google.com", "licenses/firebase")

    watcher = if (BuildConfig.DEBUG) {
      // Assign
      LeakCanary.install(this)
    } else {
      // Assign
      RefWatcher.DISABLED
    }

    startHomeNotification()

    Toasty.makeText(
        applicationContext, getString(R.string.home_button_started),
        Toasty.LENGTH_SHORT
    )
        .show()
  }

  // The application is simple, so we don't really add options to enable or disable a notification
  private fun startHomeNotification() {
    val id = 1001
    val requestCode = 1004

    val notificationChannelId = "home_button_foreground"
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      setupNotificationChannel(notificationChannelId)
    }

    val pe = PendingIntent.getActivity(
        applicationContext, requestCode, home,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    val builder = NotificationCompat.Builder(applicationContext, notificationChannelId)
        .apply {
          setContentIntent(pe)
          setSmallIcon(R.drawable.ic_home_notification)
          setOngoing(true)
          setWhen(0)
          setNumber(0)
          setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
          setContentTitle(getString(R.string.app_name))
          setContentText(getString(R.string.press_to_home))
          color = ContextCompat.getColor(applicationContext, R.color.primary)
          priority = NotificationCompat.PRIORITY_MIN
        }
    NotificationManagerCompat.from(applicationContext)
        .notify(id, builder.build())
  }

  @RequiresApi(VERSION_CODES.O)
  private fun setupNotificationChannel(
    notificationChannelId: String
  ) {
    val name = "Home Service"
    val desc = "Notification related to the Home Button service"
    val importance = NotificationManager.IMPORTANCE_MIN
    val notificationChannel = NotificationChannel(notificationChannelId, name, importance).apply {
      lockscreenVisibility = Notification.VISIBILITY_PUBLIC
      description = desc
      enableLights(false)
      enableVibration(false)
    }

    Timber.d("Create notification channel with id: %s", notificationChannelId)
    val notificationManager: NotificationManager = applicationContext.getSystemService(
        Context.NOTIFICATION_SERVICE
    ) as NotificationManager
    notificationManager.createNotificationChannel(notificationChannel)
  }

  companion object {

    @JvmStatic
    @CheckResult
    internal fun getRefWatcher(fragment: Fragment): RefWatcher {
      val application = fragment.activity!!.application
      if (application is HomeButton) {
        return application.watcher
      } else {
        throw IllegalStateException("Application is not Home Button")
      }
    }
  }
}
