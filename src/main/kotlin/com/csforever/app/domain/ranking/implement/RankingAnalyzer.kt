package com.csforever.app.domain.ranking.implement

import com.csforever.app.domain.ranking.dao.RankingDao
import org.springframework.stereotype.Component

@Component
class RankingAnalyzer(
    private val rankingDao: RankingDao
) {

    suspend fun analyzeRanking() {
        rankingDao.analyzeRanking()
    }
}