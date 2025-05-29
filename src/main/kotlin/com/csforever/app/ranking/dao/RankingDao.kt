package com.csforever.app.ranking.dao

import com.csforever.app.ranking.model.Ranking

interface RankingDao {

    suspend fun analyzeRanking()
    suspend fun findByUserId(userId: Long): Ranking?
}