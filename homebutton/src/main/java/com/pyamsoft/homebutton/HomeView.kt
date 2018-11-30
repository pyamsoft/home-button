package com.pyamsoft.homebutton

import com.pyamsoft.pydroid.ui.app.BaseScreen

interface HomeView : BaseScreen {

  fun onToolbarNavClicked(onClick: () -> Unit)
}