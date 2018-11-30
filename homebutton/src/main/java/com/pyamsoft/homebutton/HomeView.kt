package com.pyamsoft.homebutton

import com.pyamsoft.pydroid.ui.app.BaseView

interface HomeView : BaseView {

  fun onToolbarNavClicked(onClick: () -> Unit)
}