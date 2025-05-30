package com.csforever.infrastructure.database.submission

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface SubmissionCoroutineRepository : CoroutineCrudRepository<SubmissionEntity, Long> {

    @Query(
        """
        SELECT COUNT(DISTINCT question_id)
        FROM submission
        WHERE user_id = :userId AND is_correct = :isCorrect
    """
    )
    suspend fun countDistinctQuestionIdByUserIdAndCorrect(
        userId: Long,
        isCorrect: Boolean
    ): Long

    @Query(
        """
        SELECT * FROM submission
        WHERE user_id = :userId AND is_correct = :isCorrect
        ORDER BY created_at DESC
        LIMIT :size OFFSET :offset
    """
    )
    suspend fun findPageByUserIdAndIsCorrect(
        userId: Long,
        isCorrect: Boolean,
        size: Int,
        offset: Int
    ): List<SubmissionEntity>

    @Query(
        """
        SELECT * FROM submission
        WHERE user_id = :userId AND is_correct = :isCorrect AND question_id IN (:questionIds)
        ORDER BY created_at DESC
        LIMIT :size OFFSET :offset
    """
    )
    suspend fun findPageByUserIdAndIsCorrectAndQuestionIds(
        userId: Long,
        isCorrect: Boolean,
        questionIds: List<Long>,
        size: Int,
        offset: Int
    ): List<SubmissionEntity>

    suspend fun countAllByUserIdAndIsCorrect(
        userId: Long,
        isCorrect: Boolean
    ): Long

    suspend fun countAllByUserIdAndIsCorrectAndQuestionIdIn(
        userId: Long,
        isCorrect: Boolean,
        questionIds: List<Long>
    ): Long

}