package com.pyamsoft.homebutton

import androidx.preference.PreferenceScreen

internal class HomePreferencesViewImpl internal constructor(
  private val preferenceScreen: PreferenceScreen
) : HomePreferencesView {

  private val context = preferenceScreen.context

  override fun onDarkThemeClicked(onClick: (dark: Boolean) -> Unit) {
    val theme = preferenceScreen.findPreference(context.getString(R.string.dark_mode_key))
    theme.setOnPreferenceChangeListener { _, newValue ->
      if (newValue is Boolean) {
        onClick(newValue)
        return@setOnPreferenceChangeListener true
      }
      return@setOnPreferenceChangeListener false
    }
  }

  override fun onShowNotificationClicked(onClick: (show: Boolean) -> Unit) {
    val homePref = preferenceScreen.findPreference(context.getString(R.string.priority_key))

    homePref.setOnPreferenceChangeListener { _, newValue ->
      if (newValue is Boolean) {
        onClick(newValue)
        return@setOnPreferenceChangeListener true
      } else {
        return@setOnPreferenceChangeListener false
      }
    }
  }

}