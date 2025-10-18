package com.csforever.app.domain.donation.implement

import com.csforever.app.common.exception.BusinessException
import com.csforever.app.common.pagination.PageResponse
import com.csforever.app.domain.donation.dao.DonationDao
import com.csforever.app.domain.donation.exception.DonationErrorCode
import com.csforever.app.domain.donation.model.Donation
import org.springframework.stereotype.Component

@Component
class DonationFinder(
    private val donationDao: DonationDao
) {

    suspend fun findPageByUserId(
        userId: Long,
        size: Int,
        page: Int
    ): PageResponse<Donation> {
        return donationDao.findPageByUserId(userId, size, page)
    }

    suspend fun findByIdAndUserId(
        userId: Long,
        donationId: Long
    ): Donation {
        return donationDao.findByIdAndUserId(userId, donationId)
            ?: throw BusinessException(DonationErrorCode.DONATION_NOT_FOUND)
    }
}