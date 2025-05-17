package com.csforever.question.service

import com.csforever.app.common.llm.LLMClient
import com.csforever.app.question.dto.QuestionCommandResponse
import com.csforever.app.question.implement.QuestionFinder
import com.csforever.app.question.service.QuestionCommandService
import com.csforever.app.submission.implement.SubmissionInserter
import com.csforever.question.QuestionTestUtil.Companion.getQuestionSubmitRequest
import com.csforever.question.QuestionTestUtil.Companion.getQuestionSubmitResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class QuestionCommandServiceTest {
    private lateinit var questionCommandService: QuestionCommandService

    private val questionFinder: QuestionFinder = mockk(relaxed = true)
    private var llmClient: LLMClient = mockk(relaxed = true)
    private var submissionInserter: SubmissionInserter = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        questionCommandService = QuestionCommandService(
            llmClient,
            submissionInserter,
            questionFinder
        )
    }

    @Test
    fun submitQuestionTest() = runTest {
        val request = getQuestionSubmitRequest()

        val questionCommandResponse = getQuestionSubmitResponse()

        coEvery { llmClient.requestByCommand<QuestionCommandResponse.QuestionSubmitResponse>(any()) } returns questionCommandResponse

        val response = questionCommandService.submitQuestion(
            request = request,
            user = null
        )

        assert(response.isCorrect == questionCommandResponse.isCorrect)
        assert(response.feedback == questionCommandResponse.feedback)
    }
}