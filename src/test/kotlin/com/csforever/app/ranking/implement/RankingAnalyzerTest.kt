package com.csforever.app.ranking.implement

import com.csforever.app.ranking.dao.RankingDao
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class RankingAnalyzerTest {
    private val rankingDao: RankingDao = mockk(relaxed = true)
    private val rankingAnalyzer = RankingAnalyzer(rankingDao)

    @Test
    @DisplayName("Ranking 측정")
    fun `Test analyzeRanking()`() = runTest {

        coEvery { rankingAnalyzer.analyzeRanking() } returns Unit

        rankingAnalyzer.analyzeRanking()

        coVerify(exactly = 1) { rankingDao.analyzeRanking() }
    }
}