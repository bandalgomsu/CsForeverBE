package com.csforever.app.domain.ranking.dao

import com.csforever.app.domain.ranking.model.Ranking

interface RankingDao {

    suspend fun analyzeRanking()
    suspend fun findByUserId(userId: Long): Ranking?
}