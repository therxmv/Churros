package com.therxmv.churros.feature.chores.domain.model

/**
 * Date-based filter bucket used by [com.therxmv.churros.feature.chores.domain.usecase.GetChoresUseCase].
 *
 * - [TODAY]    — chores whose `due_at` falls on today's date and are not yet completed.
 * - [TOMORROW] — chores whose `due_at` falls on tomorrow's date and are not yet completed.
 * - [DONE]     — chores that have a non-null `completed_at`.
 */
enum class DateBucket { TODAY, TOMORROW, DONE }
