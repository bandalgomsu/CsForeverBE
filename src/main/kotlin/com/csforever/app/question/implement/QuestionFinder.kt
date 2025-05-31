package com.csforever.app.question.implement

import com.csforever.app.common.exception.BusinessException
import com.csforever.app.common.redis.RedisClient
import com.csforever.app.question.dao.QuestionDao
import com.csforever.app.question.exception.QuestionErrorCode
import com.csforever.app.question.model.Question
import com.csforever.app.question.model.QuestionTag
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class QuestionFinder(
    private val questionDao: QuestionDao,
    private val redisClient: RedisClient,
) {

    private val log = LoggerFactory.getLogger(QuestionFinder::class.java)

    suspend fun findById(questionId: Long): Question {
        return questionDao.findById(questionId) ?: throw BusinessException(QuestionErrorCode.QUESTION_NOT_FOUND)
    }

    suspend fun findIdsByTag(tag: QuestionTag): List<Long> {
        return questionDao.findIdsByTag(tag)
    }

    suspend fun findRandomByTags(tags: List<QuestionTag>): Question {
        val randomTag = tags.random()

        val questionIds = questionDao.findIdsByTag(randomTag)
        val randomId = questionIds.randomOrNull()
            ?: throw BusinessException(QuestionErrorCode.QUESTION_NOT_FOUND)

        return questionDao.findById(randomId) ?: throw BusinessException(QuestionErrorCode.QUESTION_NOT_FOUND)
    }

    suspend fun findAllByIdIn(questionIds: List<Long>): List<Question> {
        return questionDao.findAllByIdIn(questionIds)
    }

    suspend fun findAllByTagAndIdIn(
        tag: QuestionTag,
        questionIds: List<Long>
    ): List<Question> {
        return questionDao.findAllByTagAndIdIn(tag, questionIds)
    }
}