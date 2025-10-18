package com.csforever.app.domain.donation.service

import com.csforever.app.common.pagination.PageResponse
import com.csforever.app.domain.donation.dto.DonationQueryResponse
import com.csforever.app.domain.donation.implement.DonationFinder
import org.springframework.stereotype.Service

@Service
class DonationQueryService(
    private val donationFinder: DonationFinder
) {

    suspend fun findPageDonationByUserId(
        userId: Long,
        size: Int,
        page: Int
    ): PageResponse<DonationQueryResponse.DonationInfo> {
        val donations = donationFinder.findPageByUserId(userId, size, page)

        val response = donations.results.map {
            DonationQueryResponse.DonationInfo.from(it)
        }

        return donations.transform(response)
    }

    suspend fun findDonationByIdAndUserId(
        userId: Long,
        donationId: Long,
    ): DonationQueryResponse.DonationInfo {
        val donations = donationFinder.findByIdAndUserId(userId, donationId)

        return DonationQueryResponse.DonationInfo.from(donations)
    }
}