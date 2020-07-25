/*
 * Copyright 2019 Peter Kenji Yamanaka
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
 *
 */

package com.pyamsoft.homebutton

import android.content.Context
import androidx.annotation.CheckResult
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.pyamsoft.pydroid.core.Enforcer
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class HomeButtonPreferences @Inject internal constructor(
    context: Context
) {

    private val keyNotificationPriority: String = context.getString(R.string.priority_key)

    private val preferences by lazy {
        Enforcer.assertOffMainThread()
        PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
    }

    @CheckResult
    suspend fun notificationPriority(): Boolean = withContext(context = Dispatchers.IO) {
        Enforcer.assertOffMainThread()
        return@withContext preferences.getBoolean(keyNotificationPriority, true)
    }

    @CheckResult
    suspend fun notificationChannelId(): String = withContext(context = Dispatchers.IO) {
        Enforcer.assertOffMainThread()
        val fallback = UUID.randomUUID().toString()
        val channelId = requireNotNull(preferences.getString(KEY_CHANNEL_ID, fallback))
        preferences.edit { putString(KEY_CHANNEL_ID, channelId) }
        return@withContext channelId
    }

    suspend fun clearNotificationChannel() = withContext(context = Dispatchers.IO) {
        Enforcer.assertOffMainThread()
        preferences.edit { remove(KEY_CHANNEL_ID) }
    }

    companion object {
        private const val KEY_CHANNEL_ID = "key_notification_channel_id"
    }
}
