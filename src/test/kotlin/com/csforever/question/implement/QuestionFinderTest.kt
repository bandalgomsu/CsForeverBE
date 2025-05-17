package com.csforever.question.implement

import com.csforever.app.question.dao.QuestionDao
import com.csforever.app.question.implement.QuestionFinder
import com.csforever.question.QuestionTestUtil.Companion.getQuestion1
import com.csforever.question.QuestionTestUtil.Companion.getQuestion2
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class QuestionFinderTest {
    private lateinit var questionFinder: QuestionFinder

    private lateinit var questionDao: QuestionDao

    @BeforeEach
    fun setup() {
        questionDao = mockk(relaxed = true)

        questionFinder = QuestionFinder(questionDao)
    }

    @Test
    fun findByIdTest() = runTest {
        val question = getQuestion1()

        coEvery { questionDao.findById(question.id!!) } returns question

        val response = questionFinder.findById(question.id!!)

        assert(response.id == question.id)
        assert(response.question == question.question)
        assert(response.bestAnswer == question.bestAnswer)
        assert(response.tag == question.tag)
    }

    @Test
    fun findRandomByTagsTest() = runTest {
        val question1 = getQuestion1()
        val question2 = getQuestion2()

        val tags = listOf(question1.tag, question2.tag)

        coEvery { questionDao.findRandomByTag(tags) } returns question1

        val response = questionFinder.findRandomByTags(tags)

        assert(response.id == question1.id)
        assert(response.question == question1.question)
        assert(response.bestAnswer == question1.bestAnswer)
        assert(response.tag == question1.tag)
    }

    @Test
    fun findAllByIdInTest() = runTest {
        val question1 = getQuestion1()
        val question2 = getQuestion2()

        val questionIds = listOf(question1.id!!, question2.id!!)

        coEvery { questionDao.findAllByIdIn(questionIds) } returns listOf(question1, question2)

        val response = questionFinder.findAllByIdIn(questionIds)

        assert(response.size == 2)
        assert(response[0].id == question1.id)
        assert(response[0].question == question1.question)
        assert(response[0].bestAnswer == question1.bestAnswer)
        assert(response[0].tag == question1.tag)
        assert(response[1].id == question2.id)
        assert(response[1].question == question2.question)
        assert(response[1].bestAnswer == question2.bestAnswer)
        assert(response[1].tag == question2.tag)
    }

}