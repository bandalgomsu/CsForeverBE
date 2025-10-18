package com.csforever.app.domain.donation.dto

import com.csforever.app.domain.donation.model.Donation

class DonationQueryResponse {
    data class DonationInfo(
        val donationId: Long,
        val question: String,
        val bestAnswer: String,
        val tag: String,
        val isConfirmed: Boolean
    ) {
        companion object {
            fun from(donation: Donation): DonationInfo {
                return DonationInfo(
                    donationId = donation.id!!,
                    question = donation.question,
                    bestAnswer = donation.bestAnswer,
                    tag = donation.tag.displayName,
                    isConfirmed = donation.isConfirmed()
                )
            }
        }
    }
}