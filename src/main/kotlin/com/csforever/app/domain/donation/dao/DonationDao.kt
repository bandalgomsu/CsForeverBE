package com.csforever.app.domain.donation.dao

import com.csforever.app.common.pagination.PageResponse
import com.csforever.app.domain.donation.model.Donation

interface DonationDao {

    suspend fun insert(donation: Donation): Donation


    suspend fun findPageByUserId(
        userId: Long,
        size: Int,
        page: Int
    ): PageResponse<Donation>

    suspend fun findByIdAndUserId(
        userId: Long,
        donationId: Long
    ): Donation?

    suspend fun update(donation: Donation): Donation
}