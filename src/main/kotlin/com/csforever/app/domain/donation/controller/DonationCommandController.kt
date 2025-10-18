package com.csforever.app.domain.donation.controller

import com.csforever.app.domain.donation.dto.DonationCommandRequest
import com.csforever.app.domain.donation.service.DonationCommandService
import com.csforever.app.domain.user.auth.annotation.AuthorizationContext
import com.csforever.app.domain.user.auth.model.UserAuthorizationContext
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/donations")
class DonationCommandController(
    private val donationCommandService: DonationCommandService
) {


    @PostMapping()
    suspend fun createDonation(
        @RequestBody request: DonationCommandRequest.DonationCreateRequest,
        @AuthorizationContext authorizationContext: UserAuthorizationContext
    ): ResponseEntity<Void> {
        donationCommandService.createDonation(request, authorizationContext.user?.id!!)

        return ResponseEntity.ok().build()
    }

    @PutMapping("/{donationId}")
    suspend fun editDonation(
        @RequestBody request: DonationCommandRequest.DonationEditRequest,
        @PathVariable donationId: Long,
        @AuthorizationContext authorizationContext: UserAuthorizationContext
    ): ResponseEntity<Void> {
        donationCommandService.editDonation(authorizationContext.user?.id!!, donationId, request)

        return ResponseEntity.ok().build()
    }
}