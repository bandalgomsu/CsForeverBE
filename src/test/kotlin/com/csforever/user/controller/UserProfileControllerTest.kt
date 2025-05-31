package com.csforever.user.controller

import com.csforever.app.auth.implement.TokenHandler
import com.csforever.app.auth.model.UserAuthorizationContext
import com.csforever.app.common.pagination.PageResponse
import com.csforever.app.user.controller.UserProfileController
import com.csforever.app.user.dto.UserProfileResponse
import com.csforever.app.user.model.Position
import com.csforever.app.user.model.Role
import com.csforever.app.user.model.User
import com.csforever.app.user.service.UserProfileService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDateTime

@WebFluxTest(controllers = [UserProfileController::class])
class UserProfileControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockkBean
    private lateinit var userProfileService: UserProfileService

    @MockkBean
    private lateinit var tokenHandler: TokenHandler

    private val mockUser = User(
        id = 1L,
        email = "user@example.com",
        password = "pw",
        nickname = "tester",
        career = 3,
        role = Role.USER,
        position = Position.BACKEND
    )

    @Test
    fun getUserProfileTest() {
        val response = UserProfileResponse.UserProfile(
            id = 1L,
            email = mockUser.email,
            nickname = mockUser.nickname,
            position = "백엔드",
            career = mockUser.career,
            correctSubmissionCount = 5,
            ranking = 1
        )

        coEvery { tokenHandler.extractToken(any()) } returns UserAuthorizationContext(mockUser)
        coEvery { userProfileService.getUserProfile(mockUser.id!!) } returns response

        webTestClient.get()
            .uri("/api/v1/user/profile")
            .header("Authorization", "Bearer dummy")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.email").isEqualTo("user@example.com")
            .jsonPath("$.correctSubmissionCount").isEqualTo(5)
    }

    @Test
    fun findUserProfileSubmissionPageTest() {
        val now = LocalDateTime.now()
        val page = PageResponse(
            results = listOf(
                UserProfileResponse.UserProfileSubmission(
                    submissionId = 101L,
                    question = "2 + 2 = ?",
                    tag = "수학",
                    answer = "4",
                    feedback = "정답입니다.",
                    isCorrect = true,
                    questionId = 10L,
                    userId = 1L,
                    createdAt = now
                )
            ),
            totalPages = 1,
            totalElements = 1,
            currentPage = 1,
            pageSize = 5
        )

        coEvery { tokenHandler.extractToken(any()) } returns UserAuthorizationContext(mockUser)
        coEvery {
            userProfileService.findUserProfileSubmissionPage(
                user = mockUser,
                isCorrect = true,
                size = 5,
                page = 1
            )
        } returns page

        webTestClient.get()
            .uri { builder ->
                builder.path("/api/v1/user/profile/submissions")
                    .queryParam("isCorrect", true)
                    .queryParam("size", 5)
                    .queryParam("page", 1)
                    .build()
            }
            .header("Authorization", "Bearer dummy")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.totalElements").isEqualTo(1)
            .jsonPath("$.results[0].answer").isEqualTo("4")
            .jsonPath("$.results[0].feedback").isEqualTo("정답입니다.")
    }
}
