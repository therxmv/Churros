package com.therxmv.churros.feature.chores.domain.model

/**
 * Client-side filter applied by [com.therxmv.churros.feature.chores.domain.usecase.GetChoresUseCase]
 * to the live chore list.
 *
 * Both filters are optional and are combined with AND logic when both are set.
 *
 * @property assigneeId When non-null, only chores assigned to this profile UUID are returned.
 * @property dateBucket When non-null, limits results to the matching [DateBucket].
 */
data class ChoreFilter(
    val assigneeId: String? = null,
    val dateBucket: DateBucket? = null,
)
