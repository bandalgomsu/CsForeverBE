package com.csforever.app.domain.donation.implement

import com.csforever.app.common.exception.BusinessException
import com.csforever.app.domain.donation.dao.DonationDao
import com.csforever.app.domain.donation.exception.DonationErrorCode
import com.csforever.app.domain.donation.model.Donation
import com.csforever.app.domain.question.model.QuestionTag
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class DonationEditor(
    private val donationDao: DonationDao
) {

    @Transactional
    suspend fun edit(
        userId: Long,
        donationId: Long,
        question: String,
        bestAnswer: String,
        tag: QuestionTag,
    ): Donation {
        val donation = donationDao.findByIdAndUserId(
            userId = userId,
            donationId = donationId
        ) ?: throw BusinessException(DonationErrorCode.DONATION_NOT_FOUND)

        if (donation.isConfirmed()) {
            throw BusinessException(DonationErrorCode.ALREADY_CONFIRMED)
        }

        val updatedDonation = donation.copy(
            question = question,
            bestAnswer = bestAnswer,
            tag = tag
        )

        return donationDao.update(
            updatedDonation
        )
    }
}