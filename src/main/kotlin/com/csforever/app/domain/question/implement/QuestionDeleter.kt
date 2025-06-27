package com.csforever.app.domain.question.implement

import com.csforever.app.common.redis.RedisClient
import com.csforever.app.common.redis.RedisKey
import org.springframework.stereotype.Component

@Component
class QuestionDeleter(
    private val redisClient: RedisClient,
) {

    suspend fun deleteAllCacheByTagPatternToRedisSet(): Unit {
        redisClient.deleteAllByPattern(RedisKey.QUESTION_TAG_SET.name)
    }
}