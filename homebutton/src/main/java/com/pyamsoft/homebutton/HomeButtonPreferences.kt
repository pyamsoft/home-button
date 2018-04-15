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

import android.content.Context
import android.support.annotation.CheckResult
import android.support.v7.preference.PreferenceManager
import androidx.core.content.edit
import java.util.UUID

class HomeButtonPreferences(context: Context) {

  private val preferences =
    PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
  private val keyNotificationPriority: String = context.getString(R.string.priority_key)

  init {
    preferences.also {
      // Init to default state
      if (!it.contains(keyNotificationPriority)) {
        it.edit { putBoolean(keyNotificationPriority, true) }
      }
    }
  }

  val notificationPriority: Boolean
    @get:CheckResult get() = preferences.getBoolean(keyNotificationPriority, true)

  val notificationChannelId: String
    get() {
      val channelId = preferences.getString(KEY_CHANNEL_ID, UUID.randomUUID().toString())
      preferences.edit { putString(KEY_CHANNEL_ID, channelId) }
      return channelId
    }

  fun clearNotificationChannel() {
    preferences.edit { remove(KEY_CHANNEL_ID) }
  }

  companion object {
    private const val KEY_CHANNEL_ID = "key_notification_channel_id"
  }
}
