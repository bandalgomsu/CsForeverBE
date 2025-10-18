package com.csforever.app.donation.implement

import com.csforever.app.common.exception.BusinessException
import com.csforever.app.domain.donation.dao.DonationDao
import com.csforever.app.domain.donation.exception.DonationErrorCode
import com.csforever.app.domain.donation.implement.DonationEditor
import com.csforever.app.domain.donation.model.Donation
import com.csforever.app.domain.question.model.QuestionTag
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class DonationEditorTest {
    private val donationDao: DonationDao = mockk(relaxed = true)
    private val donationEditor = DonationEditor(donationDao)

    @Test
    fun shouldCorrect() = runTest {
        val userId = 1L
        val donationId = 1L
        val question = "Updated question?"
        val bestAnswer = "Updated best answer."
        val tag = QuestionTag.SPRING

        coEvery {
            donationDao.findByIdAndUserId(
                userId = userId,
                donationId = donationId
            )
        }.returns(
            Donation(
                id = donationId,
                question = "Original question?",
                bestAnswer = "Original best answer.",
                tag = QuestionTag.SPRING,
                userId = userId
            )
        )

        coEvery {
            donationDao.update(any())
        }.returns(
            Donation(
                id = donationId,
                question = question,
                bestAnswer = bestAnswer,
                tag = tag,
                userId = userId
            )
        )

        val result = donationEditor.edit(
            userId = userId,
            donationId = donationId,
            question = question,
            bestAnswer = bestAnswer,
            tag = tag
        )

        assert(result.question == question)
        assert(result.bestAnswer == bestAnswer)
        assert(result.tag == tag)
    }

    @Test
    fun shouldThrow_DONATION_NOT_FOUND() = runTest {
        val userId = 1L
        val donationId = 1L
        val question = "Updated question?"
        val bestAnswer = "Updated best answer."
        val tag = QuestionTag.SPRING

        coEvery {
            donationDao.findByIdAndUserId(
                userId = userId,
                donationId = donationId
            )
        }.returns(
            null
        )


        val result = assertThrows<BusinessException> {
            donationEditor.edit(
                userId = userId,
                donationId = donationId,
                question = question,
                bestAnswer = bestAnswer,
                tag = tag
            )
        }

        assert(result.errorCode.getCodeValue() == DonationErrorCode.DONATION_NOT_FOUND.code)
    }

    @Test
    fun shouldThrow_ALEADY_CONFIRMED() = runTest {
        val userId = 1L
        val donationId = 1L
        val question = "Updated question?"
        val bestAnswer = "Updated best answer."
        val tag = QuestionTag.SPRING

        coEvery {
            donationDao.findByIdAndUserId(
                userId = userId,
                donationId = donationId
            )
        }.returns(
            Donation(
                id = donationId,
                question = "Original question?",
                bestAnswer = "Original best answer.",
                tag = QuestionTag.SPRING,
                userId = userId,
                questionId = 100L // confirmed
            )
        )


        val result = assertThrows<BusinessException> {
            donationEditor.edit(
                userId = userId,
                donationId = donationId,
                question = question,
                bestAnswer = bestAnswer,
                tag = tag
            )
        }

        assert(result.errorCode.getCodeValue() == DonationErrorCode.ALREADY_CONFIRMED.code)
    }
}