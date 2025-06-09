package com.csforever.app.ranking.implement

import com.csforever.app.ranking.RankingTestUtil
import com.csforever.app.ranking.dao.RankingDao
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class RankingFinderTest {
    private val rankingDao: RankingDao = mockk(relaxed = true)

    private val rankingFinder = RankingFinder(rankingDao)

    @Test
    @DisplayName("UserId 기반 랭킹 조회")
    fun `Test findByUserId`() = runTest {
        val userId = 1L
        val correctSubmissionCount = 10L
        val ranking =
            RankingTestUtil.createTestRanking(userId = userId, correctSubmissionCount = correctSubmissionCount)

        coEvery { rankingDao.findByUserId(userId) } returns ranking

        val response = rankingFinder.findByUserId(userId)

        assertEquals(ranking.id, response.id)
        assertEquals(ranking.userId, response.userId)
        assertEquals(ranking.ranking, response.ranking)
        assertEquals(ranking.correctSubmissionCount, response.correctSubmissionCount)
    }
}