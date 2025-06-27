package com.csforever.app.domain.user.profile.controller

import com.csforever.app.domain.user.auth.annotation.AuthorizationContext
import com.csforever.app.domain.user.auth.model.UserAuthorizationContext
import com.csforever.app.common.pagination.PageResponse
import com.csforever.app.domain.question.model.QuestionTag
import com.csforever.app.domain.user.profile.dto.UserProfileRequest
import com.csforever.app.domain.user.profile.dto.UserProfileResponse
import com.csforever.app.domain.user.profile.service.UserProfileService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user/profile")
class UserProfileController(
    private val userProfileService: UserProfileService
) {

    @GetMapping
    suspend fun getUserProfile(@AuthorizationContext authorizationContext: UserAuthorizationContext): ResponseEntity<UserProfileResponse.UserProfile> {
        val response = userProfileService.getUserProfile(authorizationContext.user!!.id!!)

        return ResponseEntity.ok(response)
    }

    @PutMapping
    suspend fun updateUserProfile(
        @AuthorizationContext authorizationContext: UserAuthorizationContext,
        @RequestBody request: UserProfileRequest.UserProfileUpdateRequest
    ): ResponseEntity<Unit> {
        request.validate()

        userProfileService.updateUserProfile(authorizationContext.user!!.id!!, request)

        return ResponseEntity.ok().build()
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

    @GetMapping("/submissions/{tag}")
    suspend fun findUserProfileSubmissionPageByTag(
        @AuthorizationContext authorizationContext: UserAuthorizationContext,
        @RequestParam isCorrect: Boolean,
        @RequestParam size: Int = 5,
        @RequestParam page: Int = 1,
        @PathVariable tag: QuestionTag
    ): ResponseEntity<PageResponse<UserProfileResponse.UserProfileSubmission>> {
        val response =
            userProfileService.findUserProfileSubmissionPageByTag(
                authorizationContext.user!!,
                isCorrect,
                size,
                page,
                tag
            )

        return ResponseEntity.ok(response)
    }
}