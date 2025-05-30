package com.csforever.app.common.redis

enum class RedisKey() {
    QUESTION_TAG_SET,
    QUESTION_SUBMIT
    ;

    fun createKey(target: String): String {
        return "${this.name}:$target"
    }
}