package com.csforever.app.question.dao

import com.csforever.app.question.model.Question
import com.csforever.app.question.model.QuestionTag

interface QuestionDao {
    suspend fun findById(questionId: Long): Question?
    suspend fun findIdsByTag(tag: QuestionTag): List<Long>

    suspend fun findAll(): List<Question>
    suspend fun findAllByIdIn(questionIds: List<Long>): List<Question>
}