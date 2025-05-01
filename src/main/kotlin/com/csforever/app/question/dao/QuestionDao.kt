package com.csforever.app.question.dao

import com.csforever.app.question.model.Question

interface QuestionDao {
    suspend fun findById(questionId: Long): Question?
}