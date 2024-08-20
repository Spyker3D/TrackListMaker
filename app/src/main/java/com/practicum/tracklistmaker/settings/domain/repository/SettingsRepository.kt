package com.practicum.tracklistmaker.settings.domain.repository


interface SettingsRepository {
    fun getThemeSettings(): Boolean
    fun updateThemeSetting(isNightModeOn: Boolean)
}