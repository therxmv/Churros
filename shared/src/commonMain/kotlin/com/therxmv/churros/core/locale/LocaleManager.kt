package com.therxmv.churros.core.locale

/**
 * Platform-agnostic locale manager for runtime per-app language switching.
 *
 * Call [setLocale] from the Settings screen when the user picks a language.
 * After calling [setLocale], the calling Activity should call `recreate()` to
 * apply the new locale immediately across the entire Compose tree.
 *
 * Supported language tags: `"en"` (English), `"uk"` (Ukrainian).
 */
expect class LocaleManager() {
    /**
     * Stores [languageTag] as the app's preferred locale and applies it to the
     * platform locale system.
     *
     * @param languageTag BCP-47 language tag, e.g. `"en"` or `"uk"`.
     */
    fun setLocale(languageTag: String)

    /**
     * Returns the BCP-47 tag of the currently stored app locale, or an empty
     * string if the system default is in use.
     */
    fun getCurrentLocaleTag(): String
}
