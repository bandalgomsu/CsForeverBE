package com.csforever.infrastructure.database.donation

import com.csforever.app.domain.donation.model.Donation
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface DonationCoroutineRepository : CoroutineCrudRepository<DonationEntity, Long> {
    suspend fun findAllByUserId(userId: Long): Flow<DonationEntity>

    suspend fun countAllByUserId(userId: Long): Long

    @Query(
        """
        SELECT * FROM donation
        WHERE user_id = :userId
        ORDER BY created_at DESC
        LIMIT :size OFFSET :offset
    """
    )
    suspend fun findPageByUserId(
        userId: Long,
        size: Int,
        offset: Int
    ): List<DonationEntity>

    suspend fun findByIdAndUserId(
        id: Long,
        userId: Long
    ): DonationEntity?
}