package com.csforever.app.donation.model

import com.csforever.app.domain.donation.model.Donation
import com.csforever.app.domain.question.model.QuestionTag
import kotlin.test.Test

class DonationModelTest {

    @Test
    fun test() {
        val donation = Donation(
            id = 1L,
            question = "What is Kotlin?",
            bestAnswer = "Kotlin is a modern programming language.",
            tag = QuestionTag.SPRING,
            userId = 42L,
            questionId = null
        )

        assert(donation.isConfirmed() == false)

        val confirmedDonation = donation.confirm(1L)
        assert(confirmedDonation.isConfirmed() == true)
        assert(confirmedDonation.questionId == 1L)
    }
}