package com.csforever.app.domain.donation.service

import com.csforever.app.domain.donation.dto.DonationCommandRequest
import com.csforever.app.domain.donation.implement.DonationEditor
import com.csforever.app.domain.donation.implement.DonationInserter
import org.springframework.stereotype.Service

@Service
class DonationCommandService(
    private val donationInserter: DonationInserter,
    private val donationEditor: DonationEditor
) {

    suspend fun createDonation(
        request: DonationCommandRequest.DonationCreateRequest,
        userId: Long
    ) {
        donationInserter.insert(
            question = request.question,
            bestAnswer = request.bestAnswer,
            tag = request.tag,
            userId = userId
        )
    }

    suspend fun editDonation(
        userId: Long,
        donationId: Long,
        request: DonationCommandRequest.DonationEditRequest,
    ) {
        donationEditor.edit(
            userId = userId,
            donationId = donationId,
            question = request.question,
            bestAnswer = request.bestAnswer,
            tag = request.tag,
        )
    }
}