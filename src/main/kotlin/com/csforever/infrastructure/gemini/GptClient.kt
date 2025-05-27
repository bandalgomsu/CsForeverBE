package com.csforever.infrastructure.gemini

import com.csforever.app.common.exception.BusinessException
import com.csforever.app.common.llm.LLMClient
import com.csforever.app.common.llm.LLMCommand
import com.csforever.app.common.llm.LLMErrorCode
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

@Component
class GptClient(
    @Value("\${llm.api.gpt.key}")
    private val apiKey: String,
    private val objectMapper: ObjectMapper
) : LLMClient {

    override suspend fun <T> requestByCommand(command: LLMCommand<T>): T {
        val webClient = WebClient.builder()
            .baseUrl("https://api.openai.com/v1/responses")
            .defaultHeader("Content-Type", "application/json")
            .defaultHeader("Authorization", "Bearer $apiKey")
            .build()

        val request = mapOf(
            "model" to "gpt-4.1-mini",
            "input" to command.body
        )

        val response = webClient
            .post()
            .bodyValue(request)
            .retrieve()
            .onStatus({ it.value() == 429 }) { res ->
                res.bodyToMono(String::class.java)
                    .flatMap {
                        Mono.error(BusinessException(it, LLMErrorCode.TOO_MANY_REQUEST))
                    }
            }
            .bodyToMono<Map<String, Any>>()
            .awaitSingle()

        val jsonText = command.extractFunc(response)

        return objectMapper.readValue(jsonText, command.returnType)
    }
}