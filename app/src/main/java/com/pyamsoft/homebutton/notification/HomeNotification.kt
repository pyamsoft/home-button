package com.pyamsoft.homebutton.notification

import com.pyamsoft.pydroid.notify.NotifyData
import kotlinx.coroutines.CoroutineScope

data class HomeNotification internal constructor(
    val show: Boolean
) : NotifyData {
}