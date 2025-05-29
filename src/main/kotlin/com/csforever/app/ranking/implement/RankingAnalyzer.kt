package com.csforever.app.ranking.implement

import com.csforever.app.ranking.dao.RankingDao
import org.springframework.stereotype.Component

@Component
class RankingAnalyzer(
    private val rankingDao: RankingDao
) {

    suspend fun analyzeRanking() {
        rankingDao.analyzeRanking()
    }
}