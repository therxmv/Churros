package com.therxmv.churros.feature.auth.domain.model

import com.therxmv.churros.feature.settings.domain.model.UserProfile

/**
 * Kept as a typealias so any future platform or presentation code that references
 * the old name still compiles without changes.
 *
 * Prefer [UserProfile] directly for new code.
 */
@Deprecated(
    message = "Use UserProfile directly. AuthUser is a typealias kept for source compatibility.",
    replaceWith = ReplaceWith(
        expression = "UserProfile",
        imports = ["com.therxmv.churros.feature.settings.domain.model.UserProfile"],
    ),
)
typealias AuthUser = UserProfile
