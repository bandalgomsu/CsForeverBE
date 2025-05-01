package com.csforever.infrastructure.gemini

import com.csforever.app.common.llm.LLMClient
import com.csforever.app.common.llm.LLMCommand
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Component
class GeminiClient(
    @Value("\${llm.api.key}")
    private val apiKey: String,
    private val objectMapper: ObjectMapper
) : LLMClient {

    override suspend fun <T> requestByCommand(command: LLMCommand<T>): T {
        val webClient = WebClient.builder()
            .baseUrl("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=${apiKey}")
            .defaultHeader("Content-Type", "application/json")
            .build()

        val request = mapOf("contents" to listOf(mapOf("parts" to listOf(mapOf("text" to command.body)))))

        val response = webClient
            .post()
            .bodyValue(request)
            .retrieve()
            .bodyToMono<Map<String, Any>>()
            .awaitSingle()

        val jsonText = command.extractFunc(response)
        
        return objectMapper.readValue(jsonText, command.returnType)
    }
}