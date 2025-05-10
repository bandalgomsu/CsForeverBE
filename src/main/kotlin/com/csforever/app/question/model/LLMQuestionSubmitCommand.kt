package com.csforever.app.question.model

import com.csforever.app.common.llm.LLMCommand
import com.csforever.app.question.dto.QuestionCommandResponse

class LLMQuestionSubmitCommand(
    private val question: String,
    private val answer: String,
    private val bestAnswer: String,
    private val userNickname: String = "훌륭한 개발자"
) : LLMCommand<QuestionCommandResponse.QuestionSubmitResponse>(
    body = """
            질문: $question  
            답변: $answer  
            모범 답변: $bestAnswer  
            
            위 내용을 기반으로 사용자의 답변이 질문에 대해 정확한지 판단해주세요.  
            
            왜 정확/부정확한지에 대한 설명과 모범 답안을 제시해줘
            또한 사용자를 지칭할때는 '${userNickname}님' 이라고 지칭해주고
            줄바꿈이 필요하면 \n 을 사용해줘
            
            다음 형식의 JSON만 출력해주세요:

            {
              "isCorrect": Boolean,  // 답변이 정확하면 true, 그렇지 않으면 false
              "feedback": String    // 왜 정확/부정확한지에 대한 설명과 모범 답안을 제시
            }
             
        """.trimIndent(),
    extractFunc = fun(jsonMap: Map<String, Any>): String {
        val candidates = jsonMap["candidates"] as? List<Map<String, Any>> ?: error("Invalid response format")
        val content = candidates.firstOrNull()?.get("content") as? Map<*, *> ?: error("Missing content")
        val parts = content["parts"] as? List<Map<*, *>> ?: error("Missing parts")
        val text = parts.firstOrNull()?.get("text") as? String ?: error("Missing text")

        return text
            .replace("```json", "") // JSON 시작 부분 제거
            .replace("```", "") // JSON 끝 부분 제거
            .trim() // 양쪽 공백 제거
    },
    returnType = QuestionCommandResponse.QuestionSubmitResponse::class.java
) {
}