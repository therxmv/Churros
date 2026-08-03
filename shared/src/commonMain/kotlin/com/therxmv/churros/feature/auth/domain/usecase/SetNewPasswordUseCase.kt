package com.therxmv.churros.feature.auth.domain.usecase

import com.therxmv.churros.core.util.PasswordValidator
import com.therxmv.churros.feature.auth.domain.model.AuthError
import com.therxmv.churros.feature.auth.domain.repository.AuthRepository

class SetNewPasswordUseCase(private val repository: AuthRepository) {

    /**
     * Updates the authenticated user's password to [newPassword].
     *
     * Validates [newPassword] against [PasswordValidator] before calling Supabase;
     * returns [AuthError.WeakPassword] immediately if the password is too weak.
     */
    suspend operator fun invoke(newPassword: String): Result<Unit> {
        if (!PasswordValidator.isValid(newPassword)) {
            return Result.failure(AuthError.WeakPassword)
        }
        return repository.setNewPassword(newPassword = newPassword)
    }
}
