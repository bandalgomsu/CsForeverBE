package com.csforever.question.service

import com.csforever.app.question.implement.QuestionFinder
import com.csforever.app.question.service.QuestionQueryService
import com.csforever.app.submission.implement.SubmissionInserter
import com.csforever.question.QuestionTestUtil.Companion.getQuestion1
import com.csforever.question.QuestionTestUtil.Companion.getQuestion2
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class QuestionQueryServiceTest {
    private lateinit var questionQueryService: QuestionQueryService
    private lateinit var questionFinder: QuestionFinder
    private lateinit var submissionInserter: SubmissionInserter

    @BeforeEach
    fun setup() {
        questionFinder = mockk(relaxed = true)
        submissionInserter = mockk(relaxed = true)

        questionQueryService = QuestionQueryService(questionFinder, submissionInserter)
    }

    @Test
    fun findRandomByTagsTest() = runTest {
        val question1 = getQuestion1()
        val question2 = getQuestion2()

        val tags = listOf(question1.tag, question2.tag)

        coEvery { questionFinder.findRandomByTags(tags) } returns question1

        val questionInfo = questionQueryService.findRandomByTags(tags)

        assert(questionInfo.question.isNotEmpty())
        assert(questionInfo.question == question1.question)
    }

    @Test
    fun findBestAnswerByIdTest() = runTest {
        val question1 = getQuestion1()
        val questionId = question1.id

        coEvery { questionFinder.findById(questionId!!) } returns question1

        val user = null // 게스트 사용자로 테스트

        val questionInfo = questionQueryService.findBestAnswerById(questionId!!, user)

        assert(questionInfo.question.isNotEmpty())
        assert(questionInfo.question == question1.question)
    }
}