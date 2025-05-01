package com.csforever.app.common.llm

abstract class LLMCommand<T>(
    var body: String,
    var extractFunc: ((Map<String, Any>) -> String),
    var returnType: Class<T>
) {
}