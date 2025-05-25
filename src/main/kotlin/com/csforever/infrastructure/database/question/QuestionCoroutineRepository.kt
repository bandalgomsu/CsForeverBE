package com.csforever.infrastructure.database.question

import com.csforever.app.question.model.QuestionTag
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface QuestionCoroutineRepository : CoroutineCrudRepository<QuestionEntity, Long> {
    suspend fun countByTagIn(tags: List<QuestionTag>): Long
    suspend fun countByTag(tag: QuestionTag): Long

    @Query(
        """
        SELECT * FROM question
        WHERE tag in (:tags)
        LIMIT :size OFFSET :offset
    """
    )
    suspend fun findPageByTagIn(size: Int, offset: Int, tags: List<QuestionTag>): Flow<QuestionEntity>

    @Query(
        """
        SELECT id FROM question
        WHERE tag = :tag
    """
    )
    suspend fun findIdsByTag(tag: QuestionTag): Flow<Long>

    suspend fun findAllByIdIn(questionIds: List<Long>): Flow<QuestionEntity>
}