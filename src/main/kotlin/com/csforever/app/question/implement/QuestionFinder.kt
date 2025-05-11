package com.csforever.app.question.implement

import com.csforever.app.common.exception.BusinessException
import com.csforever.app.question.dao.QuestionDao
import com.csforever.app.question.exception.QuestionErrorCode
import com.csforever.app.question.model.Question
import com.csforever.app.question.model.QuestionTag
import org.springframework.stereotype.Component

@Component
class QuestionFinder(
    private val questionDao: QuestionDao
) {
    suspend fun findById(questionId: Long): Question {
        return questionDao.findById(questionId) ?: throw BusinessException(QuestionErrorCode.QUESTION_NOT_FOUND)
    }

    suspend fun findRandomByTags(tags: List<QuestionTag>): Question {
        return questionDao.findRandomByTag(tags) ?: throw BusinessException(QuestionErrorCode.QUESTION_NOT_FOUND)
    }

    suspend fun findAllByIdIn(questionIds: List<Long>): List<Question> {
        return questionDao.findAllByIdIn(questionIds)
    }
}