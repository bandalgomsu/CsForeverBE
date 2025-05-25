package com.csforever.question.service

import com.csforever.app.question.implement.QuestionSubmitter
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

    private val questionSubmitter: QuestionSubmitter = mockk(relaxed = true)
    private var submissionInserter: SubmissionInserter = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        questionCommandService = QuestionCommandService(
            submissionInserter,
            questionSubmitter,
        )
    }

    @Test
    fun submitQuestionTest() = runTest {
        val request = getQuestionSubmitRequest()

        val questionCommandResponse = getQuestionSubmitResponse()

        coEvery { questionSubmitter.submit(request.questionId, request.answer, null) } returns questionCommandResponse

        val response = questionCommandService.submitQuestion(
            request = request,
            user = null
        )

        assert(response.isCorrect == questionCommandResponse.isCorrect)
        assert(response.feedback == questionCommandResponse.feedback)
    }
}