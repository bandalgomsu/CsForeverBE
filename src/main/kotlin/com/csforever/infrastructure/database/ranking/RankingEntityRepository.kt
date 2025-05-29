package com.csforever.infrastructure.database.ranking

import com.csforever.app.ranking.dao.RankingDao
import com.csforever.app.ranking.model.Ranking
import org.springframework.stereotype.Component

@Component
class RankingEntityRepository(
    private val rankingRawRepository: RankingRawRepository,
    private val rankingCoroutineRepository: RankingCoroutineRepository
) : RankingDao {

    override suspend fun analyzeRanking() {
        rankingRawRepository.analyzeRanking()
    }

    override suspend fun findByUserId(userId: Long): Ranking? {
        return rankingCoroutineRepository.findByUserId(userId)?.toModel()
    }
}