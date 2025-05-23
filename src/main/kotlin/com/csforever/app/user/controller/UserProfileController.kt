package com.csforever.app.user.controller

import com.csforever.app.auth.annotation.AuthorizationContext
import com.csforever.app.auth.model.UserAuthorizationContext
import com.csforever.app.common.pagination.PageResponse
import com.csforever.app.user.dto.UserProfileResponse
import com.csforever.app.user.service.UserProfileService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user/profile")
class UserProfileController(
    private val userProfileService: UserProfileService
) {

    @GetMapping
    suspend fun getUserProfile(@AuthorizationContext authorizationContext: UserAuthorizationContext): ResponseEntity<UserProfileResponse.UserProfile> {
        val response = userProfileService.getUserProfile(authorizationContext.user!!)

        return ResponseEntity.ok(response)
    }

    @GetMapping("/submissions")
    suspend fun findUserProfileSubmissionPage(
        @AuthorizationContext authorizationContext: UserAuthorizationContext,
        @RequestParam isCorrect: Boolean,
        @RequestParam size: Int = 5,
        @RequestParam page: Int = 1,
    ): ResponseEntity<PageResponse<UserProfileResponse.UserProfileSubmission>> {
        val response =
            userProfileService.findUserProfileSubmissionPage(authorizationContext.user!!, isCorrect, size, page)

        return ResponseEntity.ok(response)
    }
}