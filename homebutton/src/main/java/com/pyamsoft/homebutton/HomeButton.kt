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

package com.pyamsoft.homebutton

import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.support.annotation.CheckResult
import android.support.v4.app.Fragment
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.NotificationCompat
import com.pyamsoft.pydroid.about.Licenses
import com.pyamsoft.pydroid.ui.PYDroid
import com.pyamsoft.pydroid.ui.helper.Toasty
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher

class HomeButton : Application() {

  private val home = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME)
  @get:CheckResult private lateinit var watcher: RefWatcher

  override fun onCreate() {
    super.onCreate()
    if (LeakCanary.isInAnalyzerProcess(this)) {
      return
    }

    PYDroid.initialize(this, BuildConfig.DEBUG)
    Licenses.create("Firebase", "https://firebase.google.com", "licenses/firebase")

    if (BuildConfig.DEBUG) {
      watcher = LeakCanary.install(this)
    } else {
      watcher = RefWatcher.DISABLED
    }

    startHomeNotification()

    Toasty.makeText(applicationContext, getString(R.string.home_button_started),
        Toasty.LENGTH_SHORT).show()
  }

  // The application is simple, so we don't really add options to enable or disable a notification
  private fun startHomeNotification() {
    val ID = 1001
    val RC = 1004

    val pe = PendingIntent.getActivity(applicationContext, RC, home,
        PendingIntent.FLAG_UPDATE_CURRENT)
    val n = NotificationCompat.Builder(applicationContext).setContentIntent(pe).setSmallIcon(
        R.mipmap.ic_launcher).setOngoing(true).setWhen(0).setNumber(0).setPriority(
        NotificationCompat.PRIORITY_MIN).setVisibility(
        NotificationCompat.VISIBILITY_PUBLIC).setColor(
        ContextCompat.getColor(applicationContext, R.color.primary)).setContentTitle(
        getString(R.string.app_name)).setContentText(getString(R.string.press_to_home)).build()
    NotificationManagerCompat.from(applicationContext).notify(ID, n)
  }

  companion object {

    @JvmStatic @CheckResult internal fun getRefWatcher(fragment: Fragment): RefWatcher {
      val application = fragment.activity.application
      if (application is HomeButton) {
        return application.watcher
      } else {
        throw IllegalStateException("Application is not Home Button")
      }
    }
  }
}
