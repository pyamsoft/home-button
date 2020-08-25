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

package com.pyamsoft.homebutton.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.CheckResult
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.pyamsoft.homebutton.R
import com.pyamsoft.pydroid.core.Enforcer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeButtonPreferences @Inject internal constructor(
    context: Context
) {

    private val keyNotificationPriority: String = context.getString(R.string.priority_key)

    private val preferences by lazy {
        Enforcer.assertOffMainThread()
        PreferenceManager.getDefaultSharedPreferences(context.applicationContext).apply {
            removeOldPreferences(this)
        }
    }

    private fun removeOldPreferences(preferences: SharedPreferences) {
        Enforcer.assertOffMainThread()
        preferences.edit {
            remove(KEY_CHANNEL_ID)
        }
    }

    @CheckResult
    suspend fun notificationPriority(): Boolean = withContext(context = Dispatchers.IO) {
        Enforcer.assertOffMainThread()
        return@withContext preferences.getBoolean(keyNotificationPriority, true)
    }

    companion object {
        private const val KEY_CHANNEL_ID = "key_notification_channel_id"
    }
}
