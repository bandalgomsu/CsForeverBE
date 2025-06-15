package com.csforever.app.term.implement

import com.csforever.app.common.exception.BusinessException
import com.csforever.app.common.llm.LLMClient
import com.csforever.app.common.llm.LLMErrorCode
import com.csforever.app.common.redis.RedisClient
import com.csforever.app.common.redis.RedisKey
import com.csforever.app.term.dao.TermDao
import com.csforever.app.term.dto.TermResponse
import com.csforever.app.term.model.LLMTermCreateCommand
import com.csforever.app.term.model.Term
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class TermFinderTest {
    private val geminiClient: LLMClient = mockk(relaxed = true)
    private val gptClient: LLMClient = mockk(relaxed = true)
    private val termDao: TermDao = mockk(relaxed = true)
    private val redisClient: RedisClient = mockk(relaxed = true)

    private val termFinder = TermFinder(
        geminiClient = geminiClient,
        gptClient = gptClient,
        termDao = termDao,
        redisClient = redisClient
    )

    @Test
    @DisplayName("용어 조회 (LLM 생성 GPT Hit)")
    fun `Test findOrCreateByTerm (LLM - GPT Hit)`() = runTest {
        val term = Term(
            id = 1L,
            term = "test Term",
            definition = "This is a test term.",
        )

        val normalTerm = term.term.trim().replace(" ", "").lowercase()
        val cacheKey = RedisKey.TERM_GET.createKey(normalTerm)


        val llmResponse = TermResponse.TermCreateResponse(
            term = normalTerm,
            definition = term.definition,
        )

        coEvery { redisClient.getData(cacheKey, Term::class.java) } returns null
        coEvery { termDao.findByTerm(normalTerm) } returns null
        coEvery { geminiClient.requestByCommand(any(LLMTermCreateCommand::class)) } throws BusinessException(
            LLMErrorCode.TOO_MANY_REQUEST
        )
        coEvery { gptClient.requestByCommand(any(LLMTermCreateCommand::class)) } returns llmResponse
        coEvery { redisClient.setData(cacheKey, any(Term::class), 60 * 60) } returns true

        val response = termFinder.findOrCreateByTerm(term.term)

        assertNotNull(response)

        assert(response.term == normalTerm)
        assert(response.definition == term.definition)
        coVerify(exactly = 1) { redisClient.setData(cacheKey, any(Term::class), 60 * 60) }
    }

    @Test
    @DisplayName("용어 조회 (LLM 생성 GPT Hit , 옯바르지 않은 용어)")
    fun `Test findOrCreateByTerm (LLM - GPT Hit , Invalid Term)`() = runTest {
        val term = Term(
            id = 1L,
            term = "test Term",
            definition = "This is a test term.",
        )

        val normalTerm = term.term.trim().replace(" ", "").lowercase()
        val cacheKey = RedisKey.TERM_GET.createKey(normalTerm)


        val llmResponse = TermResponse.TermCreateResponse(
            term = normalTerm,
            definition = ""
        )

        coEvery { redisClient.getData(cacheKey, Term::class.java) } returns null
        coEvery { termDao.findByTerm(normalTerm) } returns null
        coEvery { geminiClient.requestByCommand(any(LLMTermCreateCommand::class)) } throws BusinessException(
            LLMErrorCode.TOO_MANY_REQUEST
        )
        coEvery { gptClient.requestByCommand(any(LLMTermCreateCommand::class)) } returns llmResponse

        val response = termFinder.findOrCreateByTerm(term.term)

        assertNotNull(response)

        assert(response.term == normalTerm)
        assert(response.definition == "옳바른 용어가 아닙니다.")
        coVerify(exactly = 1) { redisClient.setData(cacheKey, any(Term::class), 60 * 60) }
    }

    @Test
    @DisplayName("용어 조회 (LLM 생성 GEMINI Hit)")
    fun `Test findOrCreateByTerm (LLM - GEMINI Hit)`() = runTest {
        val term = Term(
            id = 1L,
            term = "test Term",
            definition = "This is a test term.",
        )

        val normalTerm = term.term.trim().replace(" ", "").lowercase()
        val cacheKey = RedisKey.TERM_GET.createKey(normalTerm)


        val llmResponse = TermResponse.TermCreateResponse(
            term = normalTerm,
            definition = term.definition
        )

        coEvery { redisClient.getData(cacheKey, Term::class.java) } returns null
        coEvery { termDao.findByTerm(normalTerm) } returns null
        coEvery { geminiClient.requestByCommand(any(LLMTermCreateCommand::class)) } returns llmResponse
        coEvery { gptClient.requestByCommand(any(LLMTermCreateCommand::class)) } returns llmResponse

        val response = termFinder.findOrCreateByTerm(term.term)

        assertNotNull(response)

        assert(response.term == normalTerm)
        assert(response.definition == term.definition)
        coVerify(exactly = 1) { redisClient.setData(cacheKey, any(Term::class), 60 * 60) }
        coVerify(exactly = 0) { gptClient.requestByCommand(any(LLMTermCreateCommand::class)) }
    }

    @Test
    @DisplayName("용어 조회 (LLM 생성 GEMINI Hit , 옯바르지 않은 용어)")
    fun `Test findOrCreateByTerm (LLM - GEMINI Hit , Invalid Term)`() = runTest {
        val term = Term(
            id = 1L,
            term = "test Term",
            definition = "This is a test term.",
        )

        val normalTerm = term.term.trim().replace(" ", "").lowercase()
        val cacheKey = RedisKey.TERM_GET.createKey(normalTerm)


        val llmResponse = TermResponse.TermCreateResponse(
            term = normalTerm,
            definition = ""
        )

        coEvery { redisClient.getData(cacheKey, Term::class.java) } returns null
        coEvery { termDao.findByTerm(normalTerm) } returns null
        coEvery { geminiClient.requestByCommand(any(LLMTermCreateCommand::class)) } returns llmResponse
        coEvery { gptClient.requestByCommand(any(LLMTermCreateCommand::class)) } returns llmResponse

        val response = termFinder.findOrCreateByTerm(term.term)

        assertNotNull(response)

        assert(response.term == normalTerm)
        assert(response.definition == "옳바른 용어가 아닙니다.")
        coVerify(exactly = 1) { redisClient.setData(cacheKey, any(Term::class), 60 * 60) }
        coVerify(exactly = 0) { gptClient.requestByCommand(any(LLMTermCreateCommand::class)) }
    }

    @Test
    @DisplayName("용어 조회 (DB Hit)")
    fun `Test findOrCreateByTerm (DB - HIT)`() = runTest {
        val normalTerm = "test Term".trim().replace(" ", "").lowercase()

        val term = Term(
            id = 1L,
            term = normalTerm,
            definition = "This is a test term.",
        )

        val cacheKey = RedisKey.TERM_GET.createKey(normalTerm)


        val llmResponse = TermResponse.TermCreateResponse(
            term = normalTerm,
            definition = term.definition
        )

        coEvery { redisClient.getData(cacheKey, Term::class.java) } returns null
        coEvery { termDao.findByTerm(normalTerm) } returns term
        coEvery { geminiClient.requestByCommand(any(LLMTermCreateCommand::class)) } returns llmResponse
        coEvery { gptClient.requestByCommand(any(LLMTermCreateCommand::class)) } returns llmResponse

        val response = termFinder.findOrCreateByTerm(term.term)

        assertNotNull(response)

        assert(response.term == normalTerm)
        assert(response.definition == term.definition)
        coVerify(exactly = 1) { redisClient.setData(cacheKey, any(Term::class), 60 * 60) }
        coVerify(exactly = 0) { gptClient.requestByCommand(any(LLMTermCreateCommand::class)) }
        coVerify(exactly = 0) { geminiClient.requestByCommand(any(LLMTermCreateCommand::class)) }
    }

    @Test
    @DisplayName("용어 조회 (REDIS Hit)")
    fun `Test findOrCreateByTerm (REDIS - HIT)`() = runTest {
        val normalTerm = "test Term".trim().replace(" ", "").lowercase()

        val term = Term(
            id = 1L,
            term = normalTerm,
            definition = "This is a test term.",
        )

        val cacheKey = RedisKey.TERM_GET.createKey(normalTerm)


        val llmResponse = TermResponse.TermCreateResponse(
            term = normalTerm,
            definition = term.definition
        )

        coEvery { redisClient.getData(cacheKey, Term::class.java) } returns term
        coEvery { termDao.findByTerm(normalTerm) } returns term
        coEvery { geminiClient.requestByCommand(any(LLMTermCreateCommand::class)) } returns llmResponse
        coEvery { gptClient.requestByCommand(any(LLMTermCreateCommand::class)) } returns llmResponse

        val response = termFinder.findOrCreateByTerm(term.term)

        assertNotNull(response)

        assert(response.term == normalTerm)
        assert(response.definition == term.definition)
        coVerify(exactly = 0) { redisClient.setData(cacheKey, any(Term::class), 60 * 60) }
        coVerify(exactly = 0) { termDao.findByTerm(normalTerm) }
        coVerify(exactly = 0) { gptClient.requestByCommand(any(LLMTermCreateCommand::class)) }
        coVerify(exactly = 0) { geminiClient.requestByCommand(any(LLMTermCreateCommand::class)) }
    }
}