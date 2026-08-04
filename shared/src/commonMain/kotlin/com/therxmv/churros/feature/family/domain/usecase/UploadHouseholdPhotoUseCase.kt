package com.therxmv.churros.feature.family.domain.usecase

import com.therxmv.churros.feature.family.domain.model.Household
import com.therxmv.churros.feature.family.domain.repository.FamilyRepository

/**
 * Uploads [imageBytes] to the `family-photos` Supabase Storage bucket and stores the
 * resulting URL in `households.photo_url`.
 *
 * The file is written at `<household_id>/photo.jpg`, overwriting any previous cover photo.
 * Access to the private bucket is controlled by Supabase Storage RLS policies; only
 * authenticated household parents may write, and only household members may read.
 */
class UploadHouseholdPhotoUseCase(private val repository: FamilyRepository) {
    suspend operator fun invoke(imageBytes: ByteArray): Result<Household> =
        repository.uploadHouseholdPhoto(imageBytes)
}
