package com.therxmv.churros.core.locale

/**
 * iOS implementation of [LocaleManager].
 *
 * Full per-app language switching on iOS requires NSUserDefaults bundle language
 * override and a bundle reload — not yet implemented for the MVP.
 * The Settings language selector is Android-first for this release.
 */
actual class LocaleManager actual constructor() {

    actual fun setLocale(languageTag: String) {
        // TODO: iOS — persist languageTag to NSUserDefaults "AppleLanguages" key
        //       and trigger a bundle reload or scene reconnect for live switching.
    }

    actual fun getCurrentLocaleTag(): String = ""
}
