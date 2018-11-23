package com.pyamsoft.homebutton

interface HomePreferencesView {

  fun onShowNotificationClicked(onClick: (show: Boolean) -> Unit)

  fun onDarkThemeClicked(onClick: (dark: Boolean) -> Unit)

}