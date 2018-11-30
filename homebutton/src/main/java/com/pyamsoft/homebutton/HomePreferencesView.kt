package com.pyamsoft.homebutton

import com.pyamsoft.pydroid.ui.app.BaseView

interface HomePreferencesView : BaseView {

  fun onShowNotificationClicked(onClick: (show: Boolean) -> Unit)

}