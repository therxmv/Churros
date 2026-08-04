package com.therxmv.churros.feature.family.domain.model

import com.therxmv.churros.feature.settings.domain.model.UserProfile

/**
 * Kept as a typealias so existing code that references [FamilyMember] by name still
 * compiles without changes.
 *
 * Prefer [UserProfile] directly for new code — it is the single consolidated user/member
 * model for this project.
 *
 * Field mapping from the old data class:
 *   userId      → [UserProfile.id]
 *   householdId → [UserProfile.householdId]
 *   role        → [UserProfile.householdRole]
 *   displayName → [UserProfile.displayName]
 *   avatarUrl   → [UserProfile.avatarUrl]
 *   joinedAt    → [UserProfile.joinedAt]
 */
@Deprecated(
    message = "Use UserProfile directly. FamilyMember is a typealias kept for source compatibility.",
    replaceWith = ReplaceWith(
        expression = "UserProfile",
        imports = ["com.therxmv.churros.feature.settings.domain.model.UserProfile"],
    ),
)
typealias FamilyMember = UserProfile
