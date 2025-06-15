package com.csforever.app.common.redis

enum class RedisKey() {
    QUESTION_TAG_SET,
    QUESTION_SUBMIT,
    TERM_GET
    ;

    fun createKey(target: String): String {
        return "${this.name}:$target"
    }
}