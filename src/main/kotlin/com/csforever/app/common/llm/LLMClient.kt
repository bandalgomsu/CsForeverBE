package com.csforever.app.common.llm

interface LLMClient {
    suspend fun <T> requestByCommand(command: LLMCommand<T>): T
}