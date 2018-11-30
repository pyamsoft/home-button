package com.pyamsoft.homebutton

interface HomePreferencesView {

  fun onShowNotificationClicked(onClick: (show: Boolean) -> Unit)

}