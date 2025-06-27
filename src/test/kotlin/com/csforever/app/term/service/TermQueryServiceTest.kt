package com.csforever.app.term.service

import com.csforever.app.domain.term.implement.TermFinder
import com.csforever.app.domain.term.model.Term
import com.csforever.app.domain.term.service.TermQueryService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TermQueryServiceTest {

    private val termFinder: TermFinder = mockk(relaxed = true)

    private val service = TermQueryService(termFinder)

    @Test
    @DisplayName("용어 조회")
    fun `Test findByTerm`() = runTest {
        val term = "testTerm"
        val termModel = Term(
            id = 1L,
            term = term,
            definition = "This is a test term definition.",
        )

        coEvery { termFinder.findOrCreateByTerm(term) } returns termModel

        val response = service.findByTerm(term)

        assertNotNull(response)
        assertEquals(term, response.term)
        assertEquals(termModel.definition, response.definition)
    }
}