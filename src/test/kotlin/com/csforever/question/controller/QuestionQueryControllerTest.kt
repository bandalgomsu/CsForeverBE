package com.csforever.question.controller

import com.csforever.app.auth.implement.TokenHandler
import com.csforever.app.auth.model.UserAuthorizationContext
import com.csforever.app.question.controller.QuestionQueryController
import com.csforever.app.question.dto.QuestionQueryResponse
import com.csforever.app.question.model.QuestionTag
import com.csforever.app.question.service.QuestionQueryService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest(controllers = [QuestionQueryController::class])
class QuestionQueryControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockkBean
    private lateinit var tokenHandler: TokenHandler

    @MockkBean
    private lateinit var questionQueryService: QuestionQueryService

    @Test
    fun findRandomByTagsTest() {
        // given
        val tags = listOf(QuestionTag.JAVA, QuestionTag.SPRING)

        val expectedResponse = QuestionQueryResponse.QuestionInfo(
            questionId = 1,
            question = "test_question",
            bestAnswer = "test_answer",
        )

        coEvery { tokenHandler.extractToken(any()) } returns UserAuthorizationContext()

        coEvery {
            questionQueryService.findRandomByTags(tags)
        } returns expectedResponse

        // when & then
        webTestClient.get()
            .uri { builder ->
                builder.path("/api/v1/questions")
                    .queryParam("tags", tags.joinToString(",") { it.name })
                    .build()
            }
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.questionId").isEqualTo(1)
            .jsonPath("$.question").isEqualTo("test_question")
    }
}
