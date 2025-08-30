package com.csforever.infrastructure.llm

import com.csforever.app.common.exception.BusinessException
import com.csforever.app.common.llm.LLMClient
import com.csforever.app.common.llm.LLMCommand
import com.csforever.app.common.llm.LLMErrorCode
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class HybridClient(
    @param:Qualifier("geminiClient") private val firstClient: LLMClient,
    @param:Qualifier("gptClient") private val secondClient: LLMClient,
) : LLMClient {

    private val log = LoggerFactory.getLogger(HybridClient::class.java)

    override suspend fun <T> requestByCommand(command: LLMCommand<T>): T {
        val response = try {
            firstClient.requestByCommand(command)
        } catch (e: BusinessException) {
            if (e.errorCode == LLMErrorCode.TOO_MANY_REQUEST) {
                log.warn("[Hybrid Client] firstClient TOO_MANY_REQUEST")
                return secondClient.requestByCommand(command)
            }

            throw e
        }

        return response
    }
}

