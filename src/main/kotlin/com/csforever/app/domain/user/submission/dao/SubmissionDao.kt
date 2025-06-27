package com.csforever.app.domain.user.submission.dao

import com.csforever.app.common.pagination.PageResponse
import com.csforever.app.domain.user.submission.model.Submission

interface SubmissionDao {

    suspend fun insert(submission: Submission): Submission
    suspend fun countDistinctQuestionIdByUserIdAndCorrect(
        userId: Long,
        isCorrect: Boolean
    ): Long

    suspend fun countAllByUserId(userId: Long): Long

    suspend fun existsByUserIdAndQuestionIdAndIsCorrect(
        userId: Long,
        questionId: Long,
        isCorrect: Boolean
    ): Boolean

    suspend fun findPageByUserIdAndIsCorrect(
        userId: Long,
        isCorrect: Boolean = true,
        size: Int = 5,
        page: Int = 1
    ): PageResponse<Submission>

    suspend fun findPageByUserIdAndIsCorrectAndQuestionIds(
        userId: Long,
        isCorrect: Boolean = true,
        questionIds: List<Long>,
        size: Int = 5,
        page: Int = 1
    ): PageResponse<Submission>
}