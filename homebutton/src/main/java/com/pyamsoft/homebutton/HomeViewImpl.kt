package com.pyamsoft.homebutton

import android.view.View
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.pyamsoft.homebutton.databinding.ActivityMainBinding
import com.pyamsoft.pydroid.ui.app.activity.ActivityBase
import com.pyamsoft.pydroid.ui.util.DebouncedOnClickListener
import com.pyamsoft.pydroid.util.toDp

internal class HomeViewImpl internal constructor(
  private val activity: ActivityBase
) : HomeView, LifecycleObserver {

  private val binding: ActivityMainBinding =
    DataBindingUtil.setContentView(activity, R.layout.activity_main)

  init {
    activity.lifecycle.addObserver(this)
    setupToolbar()
  }

  @Suppress("unused")
  @OnLifecycleEvent(ON_DESTROY)
  internal fun destroy() {
    activity.lifecycle.removeObserver(this)
    binding.unbind()
  }

  override fun root(): View {
    return binding.root
  }

  private fun setupToolbar() {
    binding.toolbar.apply {
      activity.setToolbar(this)
      setTitle(R.string.app_name)
      ViewCompat.setElevation(this, 4F.toDp(context).toFloat())

      setNavigationOnClickListener(DebouncedOnClickListener.create {
        activity.onBackPressed()
      })
    }
  }

}