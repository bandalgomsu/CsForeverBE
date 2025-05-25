package com.csforever.question.implement

import com.csforever.app.common.exception.BusinessException
import com.csforever.app.common.redis.RedisClient
import com.csforever.app.common.redis.RedisKey
import com.csforever.app.question.dao.QuestionDao
import com.csforever.app.question.exception.QuestionErrorCode
import com.csforever.app.question.implement.QuestionFinder
import com.csforever.app.question.model.Question
import com.csforever.question.QuestionTestUtil.Companion.getQuestion1
import com.csforever.question.QuestionTestUtil.Companion.getQuestion2
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class QuestionFinderTest {
    private lateinit var questionFinder: QuestionFinder

    private lateinit var redisClient: RedisClient
    private lateinit var questionDao: QuestionDao

    @BeforeEach
    fun setup() {
        questionDao = mockk(relaxed = true)
        redisClient = mockk(relaxed = true)

        questionFinder = QuestionFinder(questionDao, redisClient)
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
    fun `findRandomByTags - when data is in Redis cache, it should return from cache`() = runTest {
        val question = getQuestion1()
        val tag = question.tag
        val redisKey = RedisKey.QUESTION_TAG_SET.createKey(tag.name)

        // 캐시 HIT
        coEvery { redisClient.getRandomDataFromSet(redisKey, Question::class.java) } returns question

        val result = questionFinder.findRandomByTags(listOf(tag))

        assert(result == question)
    }

    @Test
    fun `findRandomByTags - when cache miss, it should query DB and return question`() = runTest {
        val question = getQuestion1()
        val tag = question.tag
        val redisKey = RedisKey.QUESTION_TAG_SET.createKey(tag.name)

        // 캐시 MISS
        coEvery { redisClient.getRandomDataFromSet(redisKey, Question::class.java) } returns null
        coEvery { questionDao.findIdsByTag(tag) } returns listOf(question.id!!)
        coEvery { questionDao.findById(question.id!!) } returns question

        val result = questionFinder.findRandomByTags(listOf(tag))

        assert(result == question)
    }

    @Test
    fun `findRandomByTags - when cache miss and DB miss, should throw exception`() = runTest {
        val tag = getQuestion1().tag
        val redisKey = RedisKey.QUESTION_TAG_SET.createKey(tag.name)

        // 캐시 MISS + DB MISS
        coEvery { redisClient.getRandomDataFromSet(redisKey, Question::class.java) } returns null
        coEvery { questionDao.findIdsByTag(tag) } returns emptyList()

        val exception = runCatching {
            questionFinder.findRandomByTags(listOf(tag))
        }.exceptionOrNull()

        assert(exception is BusinessException)
        assert((exception as BusinessException).errorCode == QuestionErrorCode.QUESTION_NOT_FOUND)
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