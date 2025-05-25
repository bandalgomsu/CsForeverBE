package com.csforever.app.question.implement

import com.csforever.app.common.crypto.CryptoUtil
import com.csforever.app.common.exception.BusinessException
import com.csforever.app.common.llm.LLMClient
import com.csforever.app.common.redis.RedisClient
import com.csforever.app.common.redis.RedisKey
import com.csforever.app.question.dao.QuestionDao
import com.csforever.app.question.dto.QuestionCommandResponse
import com.csforever.app.question.exception.QuestionErrorCode
import com.csforever.app.question.model.LLMQuestionSubmitCommand
import com.csforever.app.user.model.User
import org.springframework.stereotype.Component

@Component
class QuestionSubmitter(
    private val llmClient: LLMClient,
    private val redisClient: RedisClient,
    private val questionDao: QuestionDao
) {

    suspend fun submit(
        questionId: Long,
        answer: String,
        user: User?
    ): QuestionCommandResponse.QuestionSubmitResponse {
        val cacheKey = RedisKey.QUESTION_SUBMIT.createKey("$questionId:${CryptoUtil.toSha256(answer)}")

        val cachedResponse = redisClient.getData(
            cacheKey,
            QuestionCommandResponse.QuestionSubmitResponse::class.java
        )

        if (cachedResponse != null) {
            return cachedResponse
        }

        val question = questionDao.findById(questionId) ?: throw BusinessException(QuestionErrorCode.QUESTION_NOT_FOUND)

        val command = LLMQuestionSubmitCommand(
            question.question,
            answer,
            question.bestAnswer,
            user?.nickname ?: "훌륭한 개발자"
        )

        val response = llmClient.requestByCommand(command)
        redisClient.setData(cacheKey, response, 60 * 5)

        return response
    }
}