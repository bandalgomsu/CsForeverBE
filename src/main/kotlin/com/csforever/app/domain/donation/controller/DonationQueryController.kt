package com.csforever.app.domain.donation.controller

import com.csforever.app.common.pagination.PageResponse
import com.csforever.app.domain.donation.dto.DonationQueryResponse
import com.csforever.app.domain.donation.service.DonationQueryService
import com.csforever.app.domain.user.auth.annotation.AuthorizationContext
import com.csforever.app.domain.user.auth.model.UserAuthorizationContext
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/donations")
class DonationQueryController(
    private val donationQueryService: DonationQueryService
) {

    @GetMapping
    suspend fun findMyDonations(
        @AuthorizationContext authorizationContext: UserAuthorizationContext,
        @RequestParam size: Int = 10,
        @RequestParam page: Int = 1
    ): ResponseEntity<PageResponse<DonationQueryResponse.DonationInfo>> {
        val response = donationQueryService.findPageDonationByUserId(
            authorizationContext.user?.id!!,
            size,
            page
        )

        return ResponseEntity.ok(response)
    }

    @GetMapping("/{donationId}")
    suspend fun findMyDonation(
        @AuthorizationContext authorizationContext: UserAuthorizationContext,
        @PathVariable donationId: Long
    ): ResponseEntity<DonationQueryResponse.DonationInfo> {
        val response = donationQueryService.findDonationByIdAndUserId(
            authorizationContext.user?.id!!,
            donationId
        )

        return ResponseEntity.ok(response)
    }
}