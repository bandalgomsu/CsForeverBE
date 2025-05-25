package com.csforever.question.implement

import com.csforever.app.common.exception.BusinessException
import com.csforever.app.common.llm.LLMClient
import com.csforever.app.common.redis.RedisClient
import com.csforever.app.question.dao.QuestionDao
import com.csforever.app.question.dto.QuestionCommandResponse
import com.csforever.app.question.exception.QuestionErrorCode
import com.csforever.app.question.implement.QuestionSubmitter
import com.csforever.app.question.model.LLMQuestionSubmitCommand
import com.csforever.app.question.model.Question
import com.csforever.app.question.model.QuestionTag
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class QuestionSubmitterTest {
    private var redisClient: RedisClient = mockk(relaxed = true)
    private var llmClient: LLMClient = mockk(relaxed = true)
    private var questionDao: QuestionDao = mockk(relaxed = true)

    private var questionSubmitter = QuestionSubmitter(
        llmClient = llmClient,
        redisClient = redisClient,
        questionDao = questionDao
    )

    val submitResponse = QuestionCommandResponse.QuestionSubmitResponse(
        isCorrect = true,
        feedback = "Great job!",
    )

    val question = Question(
        id = 1L,
        question = "What is the capital of France?",
        bestAnswer = "Paris",
        tag = QuestionTag.JAVA
    )

    @Test
    fun `submit `() = runBlocking {
        val questionId = 1L
        val answer = "Sample Answer"
        val user = null

        coEvery {
            redisClient.getData(
                any(),
                QuestionCommandResponse.QuestionSubmitResponse::class.java
            )
        } returns null

        coEvery {
            questionDao.findById(questionId)
        } returns question

        coEvery {
            llmClient.requestByCommand(any(LLMQuestionSubmitCommand::class))
        } returns submitResponse

        val response = questionSubmitter.submit(questionId, answer, user)

        assertEquals(submitResponse.feedback, response.feedback)
        assertEquals(submitResponse.isCorrect, response.isCorrect)
        coVerify(exactly = 1) { redisClient.setData(any(), submitResponse, 60 * 5) }
    }

    @Test
    fun `submit - when cache hit`() = runBlocking {
        val questionId = 1L
        val answer = "Sample Answer"
        val user = null

        coEvery {
            redisClient.getData(
                any(),
                QuestionCommandResponse.QuestionSubmitResponse::class.java
            )
        } returns submitResponse

        val response = questionSubmitter.submit(questionId, answer, user)

        assertEquals(submitResponse.feedback, response.feedback)
        assertEquals(submitResponse.isCorrect, response.isCorrect)
        coVerify(exactly = 0) { questionDao.findById(questionId) }
    }

    @Test
    fun `submit - when not found question`() = runBlocking {
        val questionId = 1L
        val answer = "Sample Answer"
        val user = null

        coEvery {
            redisClient.getData(
                any(),
                QuestionCommandResponse.QuestionSubmitResponse::class.java
            )
        } returns null

        coEvery {
            questionDao.findById(questionId)
        } returns null


        val response = assertThrows<BusinessException> {
            questionSubmitter.submit(questionId, answer, user)
        }

        assertEquals(QuestionErrorCode.QUESTION_NOT_FOUND, response.errorCode)
    }


}