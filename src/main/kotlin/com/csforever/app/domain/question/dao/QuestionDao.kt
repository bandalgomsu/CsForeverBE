package com.csforever.app.domain.question.dao

import com.csforever.app.common.pagination.PageResponse
import com.csforever.app.domain.question.model.Question
import com.csforever.app.domain.question.model.QuestionTag

interface QuestionDao {
    suspend fun findById(questionId: Long): Question?
    suspend fun findIdsByTag(tag: QuestionTag): List<Long>

    suspend fun findAll(): List<Question>
    suspend fun findAllByIdIn(questionIds: List<Long>): List<Question>
    suspend fun findAllByTagAndIdIn(tag: QuestionTag, questionIds: List<Long>): List<Question>

    suspend fun findPageByTag(tag: QuestionTag, size: Int, page: Int): PageResponse<Question>
}