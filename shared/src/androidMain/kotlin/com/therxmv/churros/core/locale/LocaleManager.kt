package com.therxmv.churros.core.locale

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

/**
 * Android implementation of [LocaleManager].
 *
 * Uses [AppCompatDelegate.setApplicationLocales] which:
 * - On API 33+: delegates to the system [android.app.LocaleManager] so the OS
 *   persists and restores the selection automatically across restarts.
 * - On API 24–32: stores the preference in SharedPreferences via AppCompat and
 *   applies it on the next Activity creation. The calling Activity should call
 *   `recreate()` for the change to take effect immediately.
 *
 * For pre-API-33 compatibility, [ChurrosApplication] overrides
 * [android.app.Application.attachBaseContext] to reapply the stored locale on
 * each cold start.
 */
actual class LocaleManager actual constructor() {

    actual fun setLocale(languageTag: String) {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(languageTag),
        )
    }

    actual fun getCurrentLocaleTag(): String {
        val locales = AppCompatDelegate.getApplicationLocales()
        return if (locales.isEmpty) "" else locales[0]?.toLanguageTag() ?: ""
    }
}
