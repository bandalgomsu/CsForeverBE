package com.csforever.question.controller

import com.csforever.app.auth.implement.TokenHandler
import com.csforever.app.auth.model.UserAuthorizationContext
import com.csforever.app.question.controller.QuestionCommandController
import com.csforever.app.question.dto.QuestionCommandRequest
import com.csforever.app.question.dto.QuestionCommandResponse
import com.csforever.app.question.service.QuestionCommandService
import com.csforever.app.user.model.Position
import com.csforever.app.user.model.Role
import com.csforever.app.user.model.User
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest(controllers = [QuestionCommandController::class])
class QuestionCommandControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockkBean
    private lateinit var questionCommandService: QuestionCommandService

    @MockkBean
    private lateinit var tokenHandler: TokenHandler

    @Test
    fun submitTest() {
        val request = QuestionCommandRequest.QuestionSubmitRequest(
            questionId = 1L,
            answer = "OOP는 객체를 기반으로 하는 설계입니다."
        )

        val response = QuestionCommandResponse.QuestionSubmitResponse(
            isCorrect = true,
            feedback = "정확한 설명입니다."
        )

        coEvery { tokenHandler.extractToken(any()) } returns UserAuthorizationContext()

        coEvery {
            questionCommandService.submitQuestion(request, null)
        } returns response


        webTestClient.post()
            .uri("/api/v1/questions")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.isCorrect").isEqualTo(true)
            .jsonPath("$.feedback").isEqualTo("정확한 설명입니다.")
    }

    @Test
    fun submitTestByLoginUser() {
        val request = QuestionCommandRequest.QuestionSubmitRequest(
            questionId = 1L,
            answer = "OOP는 객체를 기반으로 하는 설계입니다."
        )

        val response = QuestionCommandResponse.QuestionSubmitResponse(
            isCorrect = true,
            feedback = "정확한 설명입니다."
        )

        coEvery { tokenHandler.extractToken(any()) } returns UserAuthorizationContext(
            user = User(
                id = 1L,
                email = "test@test.com",
                password = "password",
                role = Role.USER,
                career = 1,
                nickname = "test_nickname",
                position = Position.BACKEND
            )
        )

        coEvery {
            questionCommandService.submitQuestion(request, any())
        } returns response


        webTestClient.post()
            .uri("/api/v1/questions")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.isCorrect").isEqualTo(true)
            .jsonPath("$.feedback").isEqualTo("정확한 설명입니다.")
    }
}