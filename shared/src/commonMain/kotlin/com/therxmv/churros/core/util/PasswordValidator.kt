package com.therxmv.churros.core.util

/**
 * Breakdown of the individual strength criteria for a password.
 *
 * Used by the Set New Password screen to drive the strength indicator UI,
 * and by [PasswordValidator.isValid] for binary pass/fail checks.
 */
data class PasswordStrength(
    /** Password has at least [PasswordValidator.MIN_LENGTH] characters. */
    val hasMinLength: Boolean,
    /** Password contains at least one digit (0–9). */
    val hasDigit: Boolean,
    /** Password contains at least one uppercase letter (A–Z). */
    val hasUppercase: Boolean,
) {
    /** `true` when all three criteria are satisfied. */
    val isValid: Boolean
        get() = hasMinLength && hasDigit && hasUppercase
}

/**
 * Stateless utility for validating and inspecting password strength.
 *
 * Rules (aligned with Supabase project settings):
 * - Minimum length: [MIN_LENGTH] characters
 * - At least one digit (0–9)
 * - At least one uppercase letter (A–Z)
 */
object PasswordValidator {

    const val MIN_LENGTH = 8

    /**
     * Returns the [PasswordStrength] breakdown for [password].
     * Useful for driving a real-time strength indicator in the UI.
     */
    fun check(password: String): PasswordStrength = PasswordStrength(
        hasMinLength = password.length >= MIN_LENGTH,
        hasDigit = password.any { it.isDigit() },
        hasUppercase = password.any { it.isUpperCase() },
    )

    /** Returns `true` when [password] satisfies all strength requirements. */
    fun isValid(password: String): Boolean = check(password).isValid
}
