package com.csforever.question.service

import com.csforever.app.question.implement.QuestionFinder
import com.csforever.app.question.service.QuestionQueryService
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

    @BeforeEach
    fun setup() {
        questionFinder = mockk(relaxed = true)
        questionQueryService = QuestionQueryService(questionFinder)
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
}