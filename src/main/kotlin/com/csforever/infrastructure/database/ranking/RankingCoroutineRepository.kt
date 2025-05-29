package com.csforever.infrastructure.database.ranking

import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface RankingCoroutineRepository : CoroutineCrudRepository<RankingEntity, Long> {
    suspend fun findByUserId(userId: Long): RankingEntity?
}