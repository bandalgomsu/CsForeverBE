package com.csforever.infrastructure.database.donation

import com.csforever.app.common.pagination.PageResponse
import com.csforever.app.domain.donation.dao.DonationDao
import com.csforever.app.domain.donation.model.Donation
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class DonationEntityRepository(
    private val donationCoroutineRepository: DonationCoroutineRepository
) : DonationDao {

    override suspend fun insert(donation: Donation): Donation {
        return donationCoroutineRepository.save(
            DonationEntity(
                question = donation.question,
                bestAnswer = donation.bestAnswer,
                tag = donation.tag,
                userId = donation.userId,
                questionId = donation.questionId,
            )
        ).toModel()
    }

    @Transactional(readOnly = true)
    override suspend fun findPageByUserId(
        userId: Long,
        size: Int,
        page: Int
    ): PageResponse<Donation> = coroutineScope {
        val safePage = maxOf(page, 1)
        val offset = (safePage - 1) * size

        val donationsDeferred = async {
            donationCoroutineRepository.findPageByUserId(
                userId = userId,
                size = size,
                offset = offset
            )
        }

        val countDeferred = async {
            donationCoroutineRepository.countAllByUserId(
                userId = userId,
            )
        }

        val donations = donationsDeferred.await()
        val totalElements = countDeferred.await()

        val totalPages = (totalElements + size - 1) / size

        return@coroutineScope PageResponse(
            results = donations.map { it.toModel() },
            totalPages = totalPages,
            totalElements = totalElements,
            currentPage = page,
            pageSize = size,
        )
    }

    override suspend fun findByIdAndUserId(
        userId: Long,
        donationId: Long
    ): Donation? {
        return donationCoroutineRepository.findByIdAndUserId(
            id = donationId,
            userId = userId
        )?.toModel()
    }

    override suspend fun update(
        donation: Donation
    ): Donation {
        return donationCoroutineRepository.save(
            DonationEntity(
                id = donation.id,
                question = donation.question,
                bestAnswer = donation.bestAnswer,
                tag = donation.tag,
                userId = donation.userId,
                questionId = donation.questionId,
                createdAt = donation.createdAt,
                updatedAt = donation.updatedAt
            )
        ).toModel()
    }
}

