package com.csforever.app.domain.donation.implement

import com.csforever.app.domain.donation.dao.DonationDao
import com.csforever.app.domain.donation.model.Donation
import com.csforever.app.domain.question.model.QuestionTag
import org.springframework.stereotype.Component

@Component
class DonationInserter(
    private val donationDao: DonationDao
) {

    suspend fun insert(
        question: String,
        bestAnswer: String,
        tag: QuestionTag,
        userId: Long
    ): Donation {
        return donationDao.insert(
            Donation(
                question = question,
                bestAnswer = bestAnswer,
                tag = tag,
                userId = userId
            )
        )
    }
}