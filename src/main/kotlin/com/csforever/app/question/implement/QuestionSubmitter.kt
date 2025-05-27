package com.csforever.app.question.implement

import com.csforever.app.common.crypto.CryptoUtil
import com.csforever.app.common.exception.BusinessException
import com.csforever.app.common.llm.LLMClient
import com.csforever.app.common.llm.LLMErrorCode
import com.csforever.app.common.llm.LLMModel
import com.csforever.app.common.redis.RedisClient
import com.csforever.app.common.redis.RedisKey
import com.csforever.app.question.dao.QuestionDao
import com.csforever.app.question.dto.QuestionCommandResponse
import com.csforever.app.question.exception.QuestionErrorCode
import com.csforever.app.question.model.LLMQuestionSubmitCommand
import com.csforever.app.user.model.User
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class QuestionSubmitter(
    @Qualifier("geminiClient") private val geminiClient: LLMClient,
    @Qualifier("gptClient") private val gptClient: LLMClient,
    private val redisClient: RedisClient,
    private val questionDao: QuestionDao
) {

    private val log = LoggerFactory.getLogger(QuestionSubmitter::class.java)

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

        val response = try {
            geminiClient.requestByCommand(command)
        } catch (e: BusinessException) {
            if (e.errorCode == LLMErrorCode.TOO_MANY_REQUEST) {
                log.warn("[QUESTION_SUBMITTER] GEMINI TOO_MANY_REQUEST")
                executeTooManyRequestFallback(command)
            }
            
            throw e
        }

        redisClient.setData(cacheKey, response, 60 * 5)
        return response
    }

    private suspend fun executeTooManyRequestFallback(beforeCommand: LLMQuestionSubmitCommand): QuestionCommandResponse.QuestionSubmitResponse {
        val command = LLMQuestionSubmitCommand(
            question = beforeCommand.question,
            answer = beforeCommand.answer,
            bestAnswer = beforeCommand.bestAnswer,
            userNickname = beforeCommand.userNickname,
            llmModel = LLMModel.GPT_4_1_MINI
        )

        return gptClient.requestByCommand(command)
    }
}